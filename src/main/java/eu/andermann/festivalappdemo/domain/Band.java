package eu.andermann.festivalappdemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class Band {

    @JsonIgnore
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
}
