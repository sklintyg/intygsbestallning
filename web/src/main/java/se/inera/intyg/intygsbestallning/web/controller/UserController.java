package se.inera.intyg.intygsbestallning.web.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.infra.security.authorities.AuthoritiesException;
import se.inera.intyg.intygsbestallning.web.auth.IntygsbestallningUser;
import se.inera.intyg.intygsbestallning.web.controller.dto.GetUserResponse;
import se.inera.intyg.intygsbestallning.web.service.user.UserService;

@RestController
@RequestMapping("/api/anvandare")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser() {
        IntygsbestallningUser user = getIbUser();
        return ResponseEntity.ok(new GetUserResponse(user));
    }

    private IntygsbestallningUser getIbUser() {
        IntygsbestallningUser user = userService.getUser();

        if (user == null) {
            throw new AuthoritiesException("No user in session");
        }

        return user;
    }
}
