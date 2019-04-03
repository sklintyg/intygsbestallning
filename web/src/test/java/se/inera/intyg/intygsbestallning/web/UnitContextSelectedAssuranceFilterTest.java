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
package se.inera.intyg.intygsbestallning.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.intygsbestallning.web.auth.IbVardenhet;
import se.inera.intyg.intygsbestallning.web.auth.IntygsbestallningUser;
import se.inera.intyg.intygsbestallning.web.service.user.UserService;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
class UnitContextSelectedAssuranceFilterTest {

    private static String IGNORED_URI = "/ignored/uri";

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;
    @Mock
    UserService userService;

    @Test
    public void testDoFilterInternalRespondsWithError() throws Exception {
        // Arrange
        UnitContextSelectedAssuranceFilter filter = new UnitContextSelectedAssuranceFilter(userService, List.of(IGNORED_URI));

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).sendError(eq(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
    }

    @Test
    public void testShouldNotFilterIgnoredURI() {
        // Arrange
        setupMocks(IGNORED_URI, false);
        UnitContextSelectedAssuranceFilter filter = new UnitContextSelectedAssuranceFilter(userService, List.of(IGNORED_URI));

        // Act & Assert
        assertTrue(filter.shouldNotFilter(request));
    }

    @Test
    public void testShouldFilterNonIgnoredURIWhenNoUnitContext() {
        // Arrange
        setupMocks("/hotstuff", false);
        UnitContextSelectedAssuranceFilter filter = new UnitContextSelectedAssuranceFilter(userService, List.of(IGNORED_URI));

        // Act & Assert
        assertFalse(filter.shouldNotFilter(request));
    }

    @Test
    public void testShouldNotFilterNonIgnoredURIWhenHavingUnitContext() {
        // Arrange
        setupMocks("/hotstuff", true);
        UnitContextSelectedAssuranceFilter filter = new UnitContextSelectedAssuranceFilter(userService, List.of(IGNORED_URI));

        // Act & Assert
        assertTrue(filter.shouldNotFilter(request));
    }


    @Test
    void doFilterInternal() {
    }

    @Test
    void shouldNotFilter() {
    }

    private void setupMocks(String requestURI, boolean hasUnitContext) {
        when(request.getRequestURI()).thenReturn(requestURI);

        IntygsbestallningUser user = new IntygsbestallningUser("userId", "userName");
        if (hasUnitContext) {
            user.setUnitContext(new IbVardenhet("id", "name", "parent", "parentName", "orgnr"));
        }
        when(userService.getUser()).thenReturn(user);

    }
}