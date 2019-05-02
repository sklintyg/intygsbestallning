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
package se.inera.intyg.intygsbestallning.web.auth.authorities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.infra.integration.hsa.services.HsaPersonService;
import se.inera.intyg.infra.security.authorities.CommonAuthoritiesResolver;
import se.inera.intyg.infra.security.authorities.bootstrap.SecurityConfigurationLoader;
import se.inera.intyg.infra.security.common.model.Privilege;
import se.inera.intyg.infra.security.common.model.RequestOrigin;
import se.inera.intyg.infra.security.common.model.Title;

import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class IntygsbestallningAuthoritiesResolverTest {

    private String authoritiesConfigurationFile = "classpath:AuthoritiesConfigurationLoaderTest/authorities-test.yaml";
    private String featuresConfigurationsFile = "classpath:AuthoritiesConfigurationLoaderTest/features-test.yaml";

    @Mock
    private HsaPersonService hsaPersonService;

    @Spy
    private SecurityConfigurationLoader configurationLoader = new SecurityConfigurationLoader(authoritiesConfigurationFile,
            featuresConfigurationsFile);

    @InjectMocks
    private CommonAuthoritiesResolver authoritiesResolver = new CommonAuthoritiesResolver();

    @BeforeEach
    public void setup() throws Exception {
        configurationLoader.afterPropertiesSet();
    }

    @Test
    public void testGetIntygsTyper() throws Exception {
        // Act
        final List<String> intygstyper = authoritiesResolver.getIntygstyper();
        // Verify
        // (We don't use this context any in Intygsbestallning)
        assertEquals(0, intygstyper.size());
    }

    @Test
    public void testGetPrivileges() throws Exception {
        // Act
        final List<Privilege> privileges = authoritiesResolver.getPrivileges();
        // Verify
        assertEquals(2, privileges.size());
    }

    @Test
    public void testGetRequestOrigins() throws Exception {
        // Act
        final List<RequestOrigin> requestOrigins = authoritiesResolver.getRequestOrigins();
        // (We don't use the origin context in Intygsbestallning)
        assertEquals(0, requestOrigins.size());
    }

    @Test
    public void testGetTitles() throws Exception {
        // Act
        final List<Title> titles = authoritiesResolver.getTitles();
        // Verify
        assertEquals(0, titles.size());
    }

}
