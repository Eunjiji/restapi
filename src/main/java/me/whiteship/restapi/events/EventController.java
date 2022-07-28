package me.whiteship.restapi.events;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    // Valid 어노테이션을 사용하면 EventDto 에 설정해놓은 정보를 들을 토대로 들어온 데이터를 검증한다.
    // 검증 후 만약 에러가 발생하거나 하는 결과값은 Errors에 담긴다.
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        // Event와 같은 객체는 java Bean 스팩을 준수하고이썽서 BeanSerializer로 json으로 변환 가능하다 but, errors는  java Bean 스팩을 준수하고 있지 않아서 json으로 변환 불가!
        if(errors.hasErrors()){
            // error가 발생하면 badRequest를 발생시킴
            return ResponseEntity.badRequest().body(errors);
        }
        
        // 위에서 바인딩할때 에러가 없으면 로직으로 검증한다.
        eventValidator.validate(eventDto, errors);
        
        // 로직 검증 후 다시 한번 에러 체크
        if(errors.hasErrors()){
            // error가 발생하면 badRequest를 발생시킴
            return ResponseEntity.badRequest().body(errors);
        }

        // 원래대로 라면 builder를 이용하여 EventDto -> Event로 바꿔줘야함
        // 생략하려면? ModelMapper 이용!
        Event event = modelMapper.map(eventDto, Event.class);

        // 아래 두줄은 보통은 service 에서 실행하는데 워낙 간단한 코드라 controller에서 진행
        event.update();     // 이벤트를 저장하기 전에 free 관련 로직 확인
        Event newEvent = eventRepository.save(event);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withSelfRel());
        eventResource.add(selfLinkBuilder.withRel("update-event"));

        return ResponseEntity.created(createdUri).body(eventResource);
    }
}
