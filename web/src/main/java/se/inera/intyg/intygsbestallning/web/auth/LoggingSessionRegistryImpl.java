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

package se.inera.intyg.intygsbestallning.web.auth;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;

/**
 * Implementation of SessionRegistry that performs audit logging of login and logout.
 */
public class LoggingSessionRegistryImpl extends SessionRegistryImpl {

    @Override
    public void registerNewSession(String sessionId, Object principal) {
        if (principal != null && principal instanceof IntygsbestallningUser) {
            IntygsbestallningUser user = (IntygsbestallningUser) principal;

            // Use the actual monitoringService once it's present
            //monitoringService.logUserLogin(user.getHsaId(), user.getAuthenticationScheme(), user.getOrigin());
        }
        super.registerNewSession(sessionId, principal);
    }

    @Override
    public void removeSessionInformation(String sessionId) {
        SessionInformation sessionInformation = getSessionInformation(sessionId);
        if (sessionInformation != null) {
            Object principal = sessionInformation.getPrincipal();

            if (principal instanceof IntygsbestallningUser) {
                IntygsbestallningUser user = (IntygsbestallningUser) principal;
                if (sessionInformation.isExpired()) {
                    // Use the actual monitoringService once it's present
                    //monitoringService.logUserSessionExpired(user.getHsaId(), user.getAuthenticationScheme());
                } else {
                    // Use the actual monitoringService once it's present
                    //monitoringService.logUserLogout(user.getHsaId(), user.getAuthenticationScheme());
                }
            }
        }
        super.removeSessionInformation(sessionId);
    }
}
