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

package se.inera.intyg.intygsbestallning.integration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import se.inera.intyg.intygsbestallning.integration.client.ClientIntegrationConfig;
import se.inera.intyg.intygsbestallning.integration.hsa.HsaConfig;
import se.inera.intyg.intygsbestallning.integration.pu.PuConfig;

@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.integration")
@Import({ClientIntegrationConfig.class,
        CacheConfigurationFromInfra.class,
        HsaConfig.class,
        PuConfig.class})
public class IntegrationConfig {
}
