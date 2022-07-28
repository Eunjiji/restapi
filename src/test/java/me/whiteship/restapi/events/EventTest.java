package me.whiteship.restapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    public void builder() throws Exception {
        //given
        Event event = Event.builder()
                .name("Spring Rest API")
                .description("REST API develpment")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() throws Exception {
        //given
        Event event = new Event();
        String name = "Event";
        String description = "Spring";

        //when
        event.setName(name);
        event.setDescription(description);

        //then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }

    @Test
    @Parameters(method = "parametersForTestFree")               // method 부분 생략 가능 -> 알아서 메소드명 보고 찾아감
    public void testFree(int basePrice, int maxPrice, boolean isFree) throws Exception {
        //given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private Object[] parametersForTestFree(){
        return new Object[]{
                new Object[] {0, 0, true},
                new Object[] {100, 0, false},
                new Object[] {0, 100, false},
                new Object[] {100, 200, false}
        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline) throws Exception{
        //given
        Event event = Event.builder()
                .location(location)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }
    
    private Object[] parametersForTestOffline() {
        return new Object[] {
                new Object[] {"강남", true},
                new Object[] {null, false},
                new Object[] {"    ", false}
        };
    }

}