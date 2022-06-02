package me.whiteship.restapi.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest // 웹과 관련된 테스트만 하는 것 -> 슬라이스 테스트
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void createEvent() throws Exception{
        //given
        mockMvc.perform(post("/api/events/")    // post 요청을 보내고 -> perform을 하고 나면 응답이 옴
                        .contentType(MediaType.APPLICATION_JSON)    // 요청에 json을 담아서 보내고 있다고 알려줌
                        .accept(MediaTypes.HAL_JSON))   // 원하는 응답의 타입
                .andExpect(status().isCreated());

        //when

        //then
    }

}