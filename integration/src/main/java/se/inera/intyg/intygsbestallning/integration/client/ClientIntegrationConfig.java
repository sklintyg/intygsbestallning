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

package se.inera.intyg.intygsbestallning.integration.client;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import se.riv.intygsbestallning.certificate.order.respondtoorder.v1.rivtabp21.RespondToOrderResponderInterface;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.integration.client")
@Profile("!myndighet-stub")
@Import({IntegrationProperties.class})
public class ClientIntegrationConfig {

    private final IntegrationProperties properties;

    public ClientIntegrationConfig(IntegrationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RespondToOrderResponderInterface respondToOrderClient() {
        JaxWsProxyFactoryBean proxyFactoryBean = new JaxWsProxyFactoryBean();
        proxyFactoryBean.setAddress(properties.getMyndighetIntegrationUrl() + properties.getRespondToOrderUrl());
        proxyFactoryBean.setServiceClass(RespondToOrderResponderInterface.class);
        return (RespondToOrderResponderInterface) proxyFactoryBean.create();
    }
}
