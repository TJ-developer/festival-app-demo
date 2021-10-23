package eu.andermann.festivalappdemo.repositories;

import eu.andermann.festivalappdemo.domain.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FestivalRepository extends JpaRepository<Festival, UUID> {
}
