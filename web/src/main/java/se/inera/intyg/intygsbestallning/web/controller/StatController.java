package se.inera.intyg.intygsbestallning.web.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsbestallning.web.auth.IbVardenhet;
import se.inera.intyg.intygsbestallning.web.service.stat.StatService;
import se.inera.intyg.intygsbestallning.web.service.user.UserService;

@RestController
@RequestMapping(StatController.API_STAT)
public class StatController {

    public static final String API_STAT = "/api/stat";

    private StatService statService;
    private UserService userService;

    public StatController(StatService statService, UserService userService) {
        this.statService = statService;
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser() {

        var user = userService.getUser();
        var hsaId = user.getUnitContext().getId();
        var orgNrVardgivare = ((IbVardenhet) user.getUnitContext()).getOrgNrVardgivare();

        return ResponseEntity.ok(statService.getStat(hsaId, orgNrVardgivare));
    }
}
