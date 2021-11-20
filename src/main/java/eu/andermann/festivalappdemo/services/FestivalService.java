package eu.andermann.festivalappdemo.services;

import eu.andermann.festivalappdemo.domain.Band;
import eu.andermann.festivalappdemo.domain.Festival;
import eu.andermann.festivalappdemo.domain.Stage;
import eu.andermann.festivalappdemo.domain.TimeSlot;
import eu.andermann.festivalappdemo.exceptions.EntityNotPresentException;
import eu.andermann.festivalappdemo.exceptions.WrongMetallicaPlayTimeException;
import eu.andermann.festivalappdemo.repositories.BandRepository;
import eu.andermann.festivalappdemo.repositories.FestivalRepository;
import eu.andermann.festivalappdemo.repositories.StageRepository;
import eu.andermann.festivalappdemo.repositories.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class FestivalService {

    private final FestivalRepository festivalRepository;
    private final BandRepository bandRepository;
    private final StageRepository stageRepository;
    private final TimeSlotRepository timeSlotRepository;

    public FestivalService(FestivalRepository festivalRepository,
                           BandRepository bandRepository,
                           StageRepository stageRepository,
                           TimeSlotRepository timeSlotRepository) {
        this.festivalRepository = festivalRepository;
        this.bandRepository = bandRepository;
        this.stageRepository = stageRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Transactional
    public void addNewBandToFestival(UUID festivalId, List<Band> bands) {
        festivalRepository.findById(festivalId)
                .orElseThrow(() -> new IllegalArgumentException("Festival with given id not found."))
                .addBands(bands);
        bandRepository.saveAll(bands);
    }

    @Transactional
    public void addStageToFestival(UUID festivalId, List<Stage> stages) {
        festivalRepository.findById(festivalId)
                .orElseThrow(() -> new IllegalArgumentException("Festival with given id not found."))
                .addStages(stages);
        stageRepository.saveAll(stages);
    }

    @Transactional
    public void addPlayingTimeForBand(UUID festivalId, Band band, Stage stage, LocalTime startTime, LocalTime endTime)
            throws WrongMetallicaPlayTimeException {
        var festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new EntityNotPresentException(festivalId, Festival.class));

        var persistedBand = festival.getBands().stream().filter(band::equals).findFirst()
                .orElseThrow(() -> new EntityNotPresentException(band.getName(), Band.class));

        var persistedStage = festival.getStages().stream().filter(stage::equals).findFirst()
                .orElseThrow(() -> new EntityNotPresentException(stage.getName(), Stage.class));

        var timeSlot = new TimeSlot(startTime, endTime);
        persistedStage.addPlayingTime(timeSlot, persistedBand);

        timeSlotRepository.save(timeSlot);
    }
}
