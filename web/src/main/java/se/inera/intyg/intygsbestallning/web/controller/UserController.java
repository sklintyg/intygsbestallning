package se.inera.intyg.intygsbestallning.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.inera.intyg.infra.security.authorities.AuthoritiesException;
import se.inera.intyg.intygsbestallning.web.auth.GetUserResponse;
import se.inera.intyg.intygsbestallning.web.auth.IntygsbestallningUser;
import se.inera.intyg.intygsbestallning.web.service.user.UserService;

@RestController
@RequestMapping(UserController.API_ANVANDARE)
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    protected static final String UNIT_CONTEXT = "/unit-context/";

    public static final String API_ANVANDARE = "/api/anvandare";
    public static final String API_UNIT_CONTEXT = API_ANVANDARE + UNIT_CONTEXT;

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser() {
        IntygsbestallningUser user = getIbUser();
        return ResponseEntity.ok(new GetUserResponse(user));
    }

    @PostMapping(path = UNIT_CONTEXT + "{hsaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity changeUnitContext(@PathVariable String hsaId) {
        IntygsbestallningUser user = getIbUser();

        LOG.debug("Attempting to change selected unit to {} for user '{}', currently selected unit is '{}'",
                user.getHsaId(),
                hsaId,
                user.getUnitContext() != null ? user.getUnitContext().getId() : "<null>");

        boolean changeSuccess = user.changeValdVardenhet(hsaId);

        if (!changeSuccess) {
            throw new AuthoritiesException(String.format("Could not change active unit context: Unit '%s' is not present in the MIUs for user '%s'",
                    hsaId, user.getHsaId()));
        }

        LOG.debug("Selected unit is now '{}'", user.getUnitContext().getId());

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
