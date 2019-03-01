package se.inera.intyg.intygsbestallning.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import se.inera.intyg.intygsbestallning.persistence.Bestallning;
import se.inera.intyg.intygsbestallning.persistence.BestallningRepository;

@RestController
@RequestMapping("/users")
public class Controller {

    @Autowired
    private BestallningRepository bestallningRepository;

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


        var bestallning = new Bestallning();

        bestallningRepository.save(bestallning);

        return ResponseEntity.ok(List.of(defaultUser, customUser, userWithBuildPattern));
    }
}
