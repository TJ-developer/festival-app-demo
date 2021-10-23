package eu.andermann.festivalappdemo;

import eu.andermann.festivalappdemo.domain.Band;
import eu.andermann.festivalappdemo.services.FestivalService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/festival")
public class FestivalController {

    private final FestivalService service;

    public FestivalController(FestivalService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{festivalId}")
    public void addBandToFestival(@PathVariable UUID festivalId, @RequestBody List<Band> bands) {
        service.addNewBandToFestival(festivalId, bands);
    }
}
