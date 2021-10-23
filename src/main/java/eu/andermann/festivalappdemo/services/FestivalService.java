package eu.andermann.festivalappdemo.services;

import eu.andermann.festivalappdemo.domain.Band;
import eu.andermann.festivalappdemo.repositories.BandRepository;
import eu.andermann.festivalappdemo.repositories.FestivalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void addNewBandToFestival(UUID festivalId, Band band) {
        var festival = festivalRepository.findById(festivalId);
        festival.orElseThrow(() -> new IllegalArgumentException("Festival with given id not found.")).addBand(band);
        bandRepository.save(band);
        festivalRepository.save(festival.get());
    }

}
