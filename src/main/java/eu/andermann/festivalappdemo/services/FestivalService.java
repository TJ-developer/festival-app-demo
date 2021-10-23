package eu.andermann.festivalappdemo.services;

import eu.andermann.festivalappdemo.domain.Band;
import eu.andermann.festivalappdemo.repositories.BandRepository;
import eu.andermann.festivalappdemo.repositories.FestivalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FestivalService {

    private final FestivalRepository festivalRepository;
    private final BandRepository bandRepository;

    public FestivalService(FestivalRepository festivalRepository, BandRepository bandRepository) {
        this.festivalRepository = festivalRepository;
        this.bandRepository = bandRepository;
    }

    @Transactional
    public void addNewBandToFestival(UUID festivalId, List<Band> bands) {
        var festival = festivalRepository.findById(festivalId);
        festival.orElseThrow(() -> new IllegalArgumentException("Festival with given id not found.")).addBands(bands);
        bandRepository.saveAll(bands);
        festivalRepository.save(festival.get());
    }

}
