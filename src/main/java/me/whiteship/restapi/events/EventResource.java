package me.whiteship.restapi.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

// BeanSerializer
// EntityModel의 getter에 이미 JsonUnwrapped가 선언 되어있음 이걸 이용해보자!
// bean이 아님
public class EventResource extends EntityModel<Event> {    // ResourceSupport -> RepresentationModel로 바뀜


    public EventResource(Event event) {
        super(event);
        /**
         * 아래 코드와 linkTo 사용한것과 같은 의미 이지만 type safe 하지 못하다! 따라서 linkTo 방법을 써주는게 좋음!
         * add(new Link("http://localhost:8080/api/events/" + event.getId()));
        */
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
