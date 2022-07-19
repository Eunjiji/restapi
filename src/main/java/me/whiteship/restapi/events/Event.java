package me.whiteship.restapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;

    private boolean offline;
    private boolean free;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    public void update() {
        // Update free
        if(this.basePrice == 0 && this.maxPrice == 0){
            this.free = true;
        }else {
            this.free = false;
        }

        //Update offline
        if(this.location == null || this.location.isBlank()){    // java 11 부터 isBlank가 공백을 다 체크해줘서 isEmpty보다 훨씬 유용함
            this.offline = false;
        }else {
            this.offline = true;
        }
    }
}
