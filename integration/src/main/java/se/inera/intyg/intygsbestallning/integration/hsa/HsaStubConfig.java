/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.intygsbestallning.integration.hsa;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import se.inera.intyg.infra.integration.hsa.stub.HsaServiceStub;

@Configuration
@ComponentScan("se.inera.intyg.infra.integration.hsa")
@ComponentScan(basePackages = {"se.inera.intyg.infra.integration.hsa.services", "se.inera.intyg.infra.integration.hsa.stub"})
@ImportResource("classpath:hsa-stub-context.xml")
@Profile({ "dev", "wc-hsa-stub" })
public class HsaStubConfig {

    @Bean
    public HsaServiceStub hsaServiceStub() {
        return new HsaServiceStub();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
