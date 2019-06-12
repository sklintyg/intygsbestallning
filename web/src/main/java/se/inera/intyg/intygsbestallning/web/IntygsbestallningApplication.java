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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import se.inera.intyg.intygsbestallning.common.CommonConfig;
import se.inera.intyg.intygsbestallning.integration.IntegrationConfig;
import se.inera.intyg.intygsbestallning.mailsender.config.MailSenderConfig;
import se.inera.intyg.intygsbestallning.persistence.PersistenceConfig;
import se.inera.intyg.intygsbestallning.web.auth.SecurityConfig;
import se.inera.intyg.intygsbestallning.web.config.SwaggerConfig;
import se.inera.intyg.intygsbestallning.web.config.WebConfig;
import se.inera.intyg.intygsbestallning.web.pdl.PdlLoggingConfig;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@Import({
        CommonConfig.class,
        IntegrationConfig.class,
        MailSenderConfig.class,
        PdlLoggingConfig.class,
        PersistenceConfig.class,
        SecurityConfig.class,
        SwaggerConfig.class,
        WebConfig.class
})
public class IntygsbestallningApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntygsbestallningApplication.class, args);
    }
}
