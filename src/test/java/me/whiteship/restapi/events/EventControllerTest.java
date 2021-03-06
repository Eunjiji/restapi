package me.whiteship.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.whiteship.restapi.common.TestDescription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest // 슬라이싱 테스트보다 웹을 개발할때는 springBootTest를 사용하는게 더 편하다 슬라이싱은 일일히 mocking 해줘야하기 때문
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();


        mockMvc.perform(post("/api/events")    // post 요청을 보내고 -> perform을 하고 나면 응답이 옴
                .contentType(MediaType.APPLICATION_JSON_UTF8)    // 요청에 json을 담아서 보내고 있다고 알려줌
                .accept(MediaTypes.HAL_JSON)   // 원하는 응답의 타입
                .content(objectMapper.writeValueAsString(event)))   // 객체를 json으로 변환
                .andDo(print()) // 실제 응답이 어떻게 나왔는지 console에서 볼수 있음
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                // 단위 테스트(EventTest.java 확인) 에서 update 관련 테스트를 진행하고 controller에서 update를 진행했기 때문에 테스트 통과 가능
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists());
    }

    @Test
    @TestDescription("입력값이 입력받을수 없는 값을 사용하는 경우에 에러가 발생가는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .offline(false)
                .build();


        mockMvc.perform(post("/api/events")    // post 요청을 보내고 -> perform을 하고 나면 응답이 옴
                .contentType(MediaType.APPLICATION_JSON)    // 요청에 json을 담아서 보내고 있다고 알려줌
                .accept(MediaTypes.HAL_JSON)   // 원하는 응답의 타입
                .content(objectMapper.writeValueAsString(event)))   // 객체를 json으로 변환
                .andDo(print()) // 실제 응답이 어떻게 나왔는지 console에서 볼수 있음
                .andExpect(status().isBadRequest());

    }

    @Test
    @TestDescription("입력값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        // 빈데이터가 들어왔을때 validation 처리
        EventDto eventDto = EventDto.builder().build();     //아무 값도 안넣어주고 보내본다
        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        // 잘못된 데이터가 들어왔을때의 validation 처리 : 이벤트 끝나는 날짜가 시작 날짜보다 빠르다. , Max가 base보다 작다 -> 잘못됨
        // 이런 경우에는 @Valid과 같은 어노테이션으로 검증이 불가능함 -> 직접 Validation을 생성해줘야함!
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())    // 여기서 부터는 응답관련 -> objectName, field... 등등이 응답에 존재했으면 좋겠다는 의미
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].rejectValue").exists())
        ;
    }

}