package eu.andermann.festivalappdemo;

import eu.andermann.festivalappdemo.domain.Band;
import eu.andermann.festivalappdemo.domain.Festival;
import eu.andermann.festivalappdemo.domain.Stage;
import eu.andermann.festivalappdemo.exceptions.WrongMetallicaPlayTimeException;
import eu.andermann.festivalappdemo.repositories.FestivalRepository;
import eu.andermann.festivalappdemo.services.FestivalService;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FestivalSteps implements En {

    @Autowired
    FestivalRepository festivalRepository;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    FestivalService service;

    UUID festivalId;
    String metallicaJson = """
            [
                {
                    "name": "Metallica"
                }
            ]""";

    String multipleBandsJson = """
            [
                {
                    "name": "Sabaton"
                },
                {
                    "name": "Five Finger Death Punch"
                }
            ]""";

    String kissJson = """
            [
                {
                    "name": "KISS"
                }
            ]
            """;

    public FestivalSteps() {
        Given("ein Festival", () -> {
            var festival = new Festival();
            festival.setName("Download Festival");
            festivalId = festivalRepository.save(festival).getId();
        });

        When("eine Band zu dem Festival hinzugefügt wird", () -> mockMvc.perform(
                        post("/festival/" + festivalId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(metallicaJson)
                )
                .andExpect(status().isCreated()));

        Then("wurde die Band zum Festival hinzugefügt.", () -> {
            var savedFestival = festivalRepository.findById(festivalId);
            assertTrue(savedFestival.isPresent());
            assertEquals(1, savedFestival.get().getBands().size());
            var bands = savedFestival.get().getBands();
            assertTrue(bands.contains(new Band("Metallica")));
        });

        When("^mehrere Bands zu dem Festival hinzugefügt werden$", () -> mockMvc.perform(
                        post("/festival/" + festivalId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(multipleBandsJson)
                )
                .andExpect(status().isCreated()));

        Then("^wurden die Bands zum Festival hinzugefügt\\.$", () -> {
            var savedFestival = festivalRepository.findById(festivalId);
            assertTrue(savedFestival.isPresent());
            assertEquals(2, savedFestival.get().getBands().size());
            var bands = savedFestival.get().getBands();
            assertTrue(bands.stream()
                    .allMatch(band -> band.getName().equals("Sabaton")
                            || band.getName().equals("Five Finger Death Punch")));
        });

        Given("^ein Festival mit der Band KISS$", () -> {
            var festival = new Festival();
            festival.setName("Download Festival");
            var kissBand = new Band();
            kissBand.setName("KISS");
            festivalId = festivalRepository.save(festival).getId();
            service.addNewBandToFestival(festivalId, List.of(kissBand));
        });
        When("^die Band KISS hinzugefügt wird$", () -> mockMvc.perform(
                        post("/festival/" + festivalId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(kissJson)
                )
                .andExpect(status().is2xxSuccessful()));
        Then("^wurde die Band nicht erneut hinzugefügt\\.$", () -> {
            var savedFestival = festivalRepository.findById(festivalId);
            assertTrue(savedFestival.isPresent());
            assertEquals(1, savedFestival.get().getBands().size());
            var bands = savedFestival.get().getBands();
            assertTrue(bands.contains(new Band("KISS")));
        });

        And("^die Bühne Main Stage$", () -> {
            var stage = new Stage();
            stage.setName("Main Stage");
            service.addStageToFestival(festivalId, List.of(stage));
        });
        And("^die Bühne Side Stage$", () -> {
            var stage = new Stage();
            stage.setName("Side Stage");
            service.addStageToFestival(festivalId, List.of(stage));
        });

        Given("^die Band Metallica$", () -> mockMvc.perform(
                        post("/festival/" + festivalId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(metallicaJson)
                )
                .andExpect(status().isCreated()));

        Then("^wurde die Spielzeit erfolgreich zugeordnet\\.$", () -> {
            var festival = festivalRepository.findById(festivalId);
            assertTrue(festival.isPresent());
            Optional<Stage> mainStage = festival.get().getStages().stream()
                    .filter(stage -> stage.equals(new Stage("Main Stage"))).findFirst();
            assertTrue(mainStage.isPresent());
            assertEquals(1, mainStage.get().getStageRunningOrder().size());
            assertTrue(mainStage.get().getStageRunningOrder().containsValue(new Band("Metallica")));
        });

        Then("^wurde die Spielzeit nicht zugeordnet\\.$", () -> {
            var festival = festivalRepository.findById(festivalId);
            assertTrue(festival.isPresent());
            Optional<Stage> mainStage = festival.get().getStages().stream()
                    .filter(stage -> stage.equals(new Stage("Main Stage"))).findFirst();
            assertTrue(mainStage.isPresent());
            assertEquals(0, mainStage.get().getStageRunningOrder().size());
        });

        When("^Metallica die Spielzeit von (\\d+) Uhr bis (\\d+):(\\d+) Uhr zugeordnet wird$",
                (Integer startTimeHour, Integer endTimeHour, Integer endTimeMinutes) -> {
                    try {
                        service.addPlayingTimeForBand(festivalId,
                                new Band("Metallica"),
                                new Stage("Main Stage"),
                                LocalTime.of(startTimeHour, 0),
                                LocalTime.of(endTimeHour, endTimeMinutes)
                        );
                    } catch (WrongMetallicaPlayTimeException e) {
                        // Nothing to do here.
                    }
                });
    }
}
