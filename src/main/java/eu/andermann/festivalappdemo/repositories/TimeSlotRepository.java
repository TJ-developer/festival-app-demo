package eu.andermann.festivalappdemo.repositories;

import eu.andermann.festivalappdemo.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, UUID> {
}
