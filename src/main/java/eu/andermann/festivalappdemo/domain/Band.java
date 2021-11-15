package eu.andermann.festivalappdemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Band {

    @JsonIgnore
    @Id
    @GeneratedValue
    private UUID id;
    private String name;

    public Band(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Band band = (Band) o;
        return name.equals(band.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
