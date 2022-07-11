package me.whiteship.restapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0){ // 비즈니스 로직 에러
            errors.rejectValue("basePrice", "wrongValue", "basePrice is wrong.");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong.");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||           // 이벤트 끝 날짜가 시작날짜보다 먼저(빠르면) 안됨
        endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||         // 이벤트 끝 날짜가 등록 마감일보다 먼저이면 안됨
        endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){          // 이벤트 끝 날짜가 이벤트 등록 시작일 보다 빠르면 안됨
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
        }

        // TODO BeginEventDateTime
        LocalDateTime beginEventDateTime = eventDto.getBeginEventDateTime();
        if(beginEventDateTime.isAfter(eventDto.getEndEventDateTime()) ||                     // 이벤트 시작 날짜가 이벤트 종료 날짜보다 늦으면 안됨
                beginEventDateTime.isAfter(eventDto.getCloseEnrollmentDateTime()) ||         // 이벤트 시작 날짜가 등록 마감일 보다 늦으면 안됨
                beginEventDateTime.isAfter(eventDto.getBeginEnrollmentDateTime())){          // 이벤트 시작 날짜가 이벤트 등록 시작일 보다 늦으면 안됨
            errors.rejectValue("beginEventDateTime", "wrongValue", "beginEventDateTime is wrong");
        }

        // TODO CloseEnrollmentDateTime
        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
        if(closeEnrollmentDateTime.isBefore(eventDto.getBeginEventDateTime()) ||                    // 이벤트 마감 날짜가 시작날짜보다 먼저(빠르면) 안됨
                closeEnrollmentDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||          // 이벤트 마감 날짜가 이벤트 등록 시작일 보다 빠르면 안됨
                closeEnrollmentDateTime.isAfter(eventDto.getEndEventDateTime())){                   // 이벤트 마감 날짜가 이벤트 끝 날짜 보다 늦으면 안됨
            errors.rejectValue("closeEnrollmentDateTime", "wrongValue", "closeEnrollmentDateTime is wrong");
        }

    }
}
