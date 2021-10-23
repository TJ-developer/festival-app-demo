package eu.andermann.festivalappdemo.repositories;

import eu.andermann.festivalappdemo.domain.Band;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BandRepository extends JpaRepository<Band, UUID> {
}
