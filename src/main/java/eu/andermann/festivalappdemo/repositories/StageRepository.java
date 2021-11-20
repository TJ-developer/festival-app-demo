package eu.andermann.festivalappdemo.repositories;

import eu.andermann.festivalappdemo.domain.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StageRepository extends JpaRepository<Stage, UUID> {

    Optional<Stage> findStageByName(String name);
}
