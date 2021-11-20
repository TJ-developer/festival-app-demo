package eu.andermann.festivalappdemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.andermann.festivalappdemo.exceptions.WrongMetallicaPlayTimeException;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Stage {

    public Stage(String name) {
        this.name = name;
    }

    @JsonIgnore
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    private Map<TimeSlot, Band> stageRunningOrder;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stage stage = (Stage) o;
        return Objects.equals(name, stage.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void addPlayingTime(TimeSlot timeSlot, Band persistedBand) throws WrongMetallicaPlayTimeException {
        checkPlayingTime(timeSlot, persistedBand);
        stageRunningOrder.put(timeSlot, persistedBand);
    }

    private void checkPlayingTime(TimeSlot timeSlot, Band persistedBand) throws WrongMetallicaPlayTimeException {
        if (persistedBand.equals(new Band("Metallica"))
                && timeSlot.getStartTime().isBefore(LocalTime.of(19, 0))) {
            throw new WrongMetallicaPlayTimeException();
        }
    }
}
