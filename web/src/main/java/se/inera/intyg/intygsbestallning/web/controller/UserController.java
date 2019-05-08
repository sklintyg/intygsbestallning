/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygsbestallning.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.inera.intyg.infra.security.authorities.AuthoritiesException;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.web.auth.ApiErrorResponse;
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

    /**
     * This method is called every time the client app boots up to determine if user is authenticated or not.
     * To avoid having {@link RequestErrorController} cluttering up the serverlog with unwanted exceptions when visiting the
     * startup page / after logging out, we allow ALL requests to enter this method (permitAll in spring security).
     * We then manually determine a suitable response based on whether we actually have a user context or not.
     * 
     * @return
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser() {
        IntygsbestallningUser user = userService.getUser();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiErrorResponse("No user in session", IbErrorCodeEnum.BESTALLNING_FEL008_ATKOMST_NEKAD.name(), ""));
        } else {
            return ResponseEntity.ok(new GetUserResponse(user));
        }
    }

    @PostMapping(path = UNIT_CONTEXT + "{hsaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity changeUnitContext(@PathVariable String hsaId) {
        IntygsbestallningUser user = userService.getUser();
        if (user == null) {
            throw new AuthoritiesException("No user in session");
        }

        LOG.debug("Attempting to change selected unit to {} for user '{}', currently selected unit is '{}'",
                user.getHsaId(),
                hsaId,
                user.getUnitContext() != null ? user.getUnitContext().getId() : "<null>");

        boolean changeSuccess = user.changeValdVardenhet(hsaId);

        if (!changeSuccess) {
            throw new AuthoritiesException(
                    String.format("Could not change active unit context: Unit '%s' is not present in the MIUs for user '%s'",
                            hsaId, user.getHsaId()));
        }

        LOG.debug("Selected unit is now '{}'", user.getUnitContext().getId());

        return ResponseEntity.ok(new GetUserResponse(user));
    }
}
