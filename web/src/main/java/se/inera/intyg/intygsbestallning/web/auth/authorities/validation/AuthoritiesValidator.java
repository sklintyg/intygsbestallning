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

package se.inera.intyg.intygsbestallning.web.auth.authorities.validation;

import java.util.Optional;
import se.inera.intyg.infra.security.authorities.validation.AuthExpectationSpecImpl;
import se.inera.intyg.infra.security.authorities.validation.AuthExpectationSpecification;
import se.inera.intyg.infra.security.common.model.IntygUser;

/**
 * Utility class that makes it easy to express and enforce authority constraint rules in backend code.
 */
public final class AuthoritiesValidator {

    /**
     * Create a expectation context with just a user.
     *
     * @param user - user in session
     * @return specification
     */
    public AuthExpectationSpecification given(IntygUser user) {
        return new AuthExpectationSpecImpl(user, Optional.empty());
    }

}
