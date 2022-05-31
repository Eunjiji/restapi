package me.whiteship.restapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class EventTest {

    @Test
    public void builder() throws Exception{
        //given
        Event event = Event.builder()
                .name("Spring Rest API")
                .description("REST API develpment")
                .build();
        assertThat(event).isNotNull();
    }
    
    @Test
    public void javaBean() throws Exception{
        //given
        Event event =  new Event();
        String name = "Event";
        String description = "Spring";

        //when
        event.setName(name);
        event.setDescription(description);

        //then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }

}