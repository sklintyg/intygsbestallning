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

package se.inera.intyg.intygsbestallning.web.pdl;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import se.inera.intyg.intygsbestallning.common.property.ActiveMqProperties;
import se.inera.intyg.intygsbestallning.common.property.PdlLoggingProperties;

import javax.jms.ConnectionFactory;

/**
 * @author Magnus Ekstrand on 2019-05-01.
 */

@EnableJms
@Configuration
@ComponentScan(basePackages = {
        "se.inera.intyg.intygsbestallning.common"})
public class PdlLoggingConfig {

    private final ActiveMqProperties activeMqProperties;
    private final PdlLoggingProperties pdlLoggingProperties;

    public PdlLoggingConfig(ActiveMqProperties activeMqProperties, PdlLoggingProperties pdlLoggingProperties) {
        this.activeMqProperties = activeMqProperties;
        this.pdlLoggingProperties = pdlLoggingProperties;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(
                activeMqProperties.getUsername(),
                activeMqProperties.getPassword(),
                activeMqProperties.getUrl());
    }

    @Bean
    public JmsTemplate jmsPdlLoggingTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setDefaultDestinationName(pdlLoggingProperties.getQueueName());
        return jmsTemplate;
    }

}