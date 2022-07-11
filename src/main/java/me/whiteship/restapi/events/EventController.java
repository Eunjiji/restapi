package me.whiteship.restapi.events;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
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

    // Valid 어노테이션을 사용하면 EventDto 에 설정해놓은 정보를 들을 토대로 들어온 데이터를 검증한다.
    // 검증 후 만약 에러가 발생하거나 하는 결과값은 Errors에 담긴다.
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if(errors.hasErrors()){
            // error가 발생하면 badRequest를 발생시킴
            return ResponseEntity.badRequest().build();
        }

        // 원래대로 라면 builder를 이용하여 EventDto -> Event로 바꿔줘야함
        // 생략하려면? ModelMapper 이용!
        Event event = modelMapper.map(eventDto, Event.class);
        
        Event newEvent = eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(event);
    }
}
