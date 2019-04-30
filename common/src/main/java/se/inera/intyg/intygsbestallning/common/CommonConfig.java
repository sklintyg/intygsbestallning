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
package se.inera.intyg.intygsbestallning.common;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import se.inera.intyg.intygsbestallning.common.property.ActiveMqProperties;
import se.inera.intyg.intygsbestallning.common.property.BestallningProperties;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.inera.intyg.intygsbestallning.common.property.MailProperties;
import se.inera.intyg.intygsbestallning.common.property.MailSenderProperties;
import se.inera.intyg.intygsbestallning.common.property.PdlLoggingProperties;
import se.inera.intyg.intygsbestallning.common.property.PersistenceProperties;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.common")
@EnableConfigurationProperties
@Import({
        BestallningProperties.class,
        IntegrationProperties.class,
        MailProperties.class,
        MailSenderProperties.class,
        PdlLoggingProperties.class,
        PersistenceProperties.class,
        ActiveMqProperties.class
})
public class CommonConfig {
}
