package se.inera.intyg.intygsbestallning.web.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import se.inera.intyg.intygsbestallning.persistence.Bestallning;
import se.inera.intyg.intygsbestallning.persistence.BestallningRepository;
import se.inera.intyg.intygsbestallning.persistence.Utredning;
import se.inera.intyg.intygsbestallning.persistence.UtredningRepository;

@RestController
@RequestMapping("/users")
public class Controller {

    private BestallningRepository bestallningRepository;
    private UtredningRepository utredningRepository;

    public Controller(BestallningRepository bestallningRepository, UtredningRepository utredningRepository) {
        this.bestallningRepository = bestallningRepository;
        this.utredningRepository = utredningRepository;
    }

    @GetMapping
    public ResponseEntity users() {

        var defaultUser = new User();

        var defaultAddress = new Address();
        var customUser = new User(2L, "CustomTolvan", "Twelvansson", defaultAddress);

        var userWithBuildPattern = new UserWithBuilderPattern.Builder()
                .id(3L)
                .firstName("firstName")
                .lastName("lastname")
                .address(defaultAddress)
                .build();

        var bestallning = new Bestallning.Builder()
                .vardenhetHsaId("1")
                .vardenehetOrgId("1")
                .build();

        bestallningRepository.save(bestallning);

        var utredning = new Utredning.Builder()
                .bestallning(bestallning)
                .build();

        utredningRepository.save(utredning);

        return ResponseEntity.ok(List.of(defaultUser, customUser, userWithBuildPattern));
    }
}
