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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.inera.intyg.infra.security.filter.SessionTimeoutFilter;
import se.inera.intyg.intygsbestallning.common.dto.StatResponse;
import se.inera.intyg.intygsbestallning.web.auth.GetSessionStatResponse;
import se.inera.intyg.intygsbestallning.web.auth.IbVardenhet;
import se.inera.intyg.intygsbestallning.web.auth.SessionState;
import se.inera.intyg.intygsbestallning.web.service.stat.StatService;
import se.inera.intyg.intygsbestallning.web.service.user.UserService;

/**
 * Reports basic information about the current session status and user stats (if authenticated).
 * This controller works in cooperation with SessionTimeoutFilter that makes sure that requests to:
 * <ul>
 * <li>getSessionStatus does NOT extend the session</li>
 * <li>returns current stats (if available)</li>
 * </ul>
 *
 * @see SessionTimeoutFilter
 * @see org.springframework.security.web.context.SecurityContextRepository SecurityContextRepository
 * @see HttpSessionSecurityContextRepository
 *      HttpSessionSecurityContextRepository
 */

@RestController
@RequestMapping(SessionStatController.SESSION_STAT_REQUEST_MAPPING)
public class SessionStatController {

    public static final String SESSION_STAT_REQUEST_MAPPING = "/public-api/session-stat";
    public static final String SESSION_STATUS_PING = "/ping";

    public static final String SESSION_STATUS_CHECK_URI = SESSION_STAT_REQUEST_MAPPING + SESSION_STATUS_PING;

    private StatService statService;
    private UserService userService;

    public SessionStatController(StatService statService, UserService userService) {
        this.statService = statService;
        this.userService = userService;
    }
    @RequestMapping(value = SessionStatController.SESSION_STATUS_PING, method = RequestMethod.GET)
    public GetSessionStatResponse getSessionStatus(HttpServletRequest request) {
        return createStatusResponse(request);
    }

    private GetSessionStatResponse createStatusResponse(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // The sessionTimeoutFilter should have put a secondsLeft attribute in the request for us to use.
        Long secondsLeft = (Long) request.getAttribute(SessionTimeoutFilter.SECONDS_UNTIL_SESSIONEXPIRE_ATTRIBUTE_KEY);
        final boolean isAuthenticated = hasAuthenticatedPrincipalSession(session);
        return new GetSessionStatResponse(new SessionState(session != null, isAuthenticated,
                secondsLeft == null ? 0 : secondsLeft), isAuthenticated ? getStats() : null);
    }

    private StatResponse getStats() {
        var user = userService.getUser();
        if (user!=null &&  user.getUnitContext() !=null) {
            var hsaId = user.getUnitContext().getId();
            var orgNrVardgivare = ((IbVardenhet) user.getUnitContext()).getOrgNrVardgivare();

            return statService.getStat(hsaId, orgNrVardgivare);
        } else {
            return null;
        }

    }

    private boolean hasAuthenticatedPrincipalSession(HttpSession session) {
        if (session != null) {
            final Object context = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            if (context != null && context instanceof SecurityContext) {
                SecurityContext securityContext = (SecurityContext) context;
                return securityContext.getAuthentication() != null && securityContext.getAuthentication().getPrincipal() != null;
            }

        }
        return false;
    }

}
