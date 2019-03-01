package se.inera.intyg.intygsbestallning.web.user;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class Controller {

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

        return ResponseEntity.ok(List.of(defaultUser, customUser, userWithBuildPattern));
    }
}
