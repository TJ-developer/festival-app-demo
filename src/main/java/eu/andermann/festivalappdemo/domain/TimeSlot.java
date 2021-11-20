package eu.andermann.festivalappdemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class TimeSlot {

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) throw new IllegalArgumentException("Timeslot end time is before start time.");
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @JsonIgnore
    @Id
    @GeneratedValue
    private UUID id;

    private LocalTime startTime;
    private LocalTime endTime;
}
