package eu.andermann.festivalappdemo;

import eu.andermann.festivalappdemo.domain.Festival;
import eu.andermann.festivalappdemo.repositories.FestivalRepository;
import eu.andermann.festivalappdemo.services.FestivalService;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
    String json = """
            {
                "name": "Metallica"
            }""";

    public FestivalSteps() {
        Given("ein Festival", () -> {
            var festival = new Festival();
            festival.setName("Download Festival");
            festivalId = festivalRepository.save(festival).getId();
        });

        When("eine Band zu dem Festival hinzugefügt wird", () -> mockMvc.perform(
                        post("/festival/" + festivalId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated()));

        Then("wurde die Band zum Festival hinzugefügt.", () -> {
            var savedFestival = festivalRepository.findById(festivalId);
            assertTrue(savedFestival.isPresent());
            assertEquals(1, savedFestival.get().getBands().size());
            var band = savedFestival.get().getBands().get(0);
            assertEquals("Metallica", band.getName());
        });
    }
}
