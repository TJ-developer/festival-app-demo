package eu.andermann.festivalappdemo.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Festival {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Band> bands;

    public void addBands(List<Band> bands) {
        this.bands.addAll(bands);
    }

}
