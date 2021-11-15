package eu.andermann.festivalappdemo;

import eu.andermann.festivalappdemo.domain.Band;
import eu.andermann.festivalappdemo.domain.Festival;
import eu.andermann.festivalappdemo.repositories.FestivalRepository;
import eu.andermann.festivalappdemo.services.FestivalService;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
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
    }
}
