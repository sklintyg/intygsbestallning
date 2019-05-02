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

package se.inera.intyg.intygsbestallning.mailsender.config;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import se.inera.intyg.intygsbestallning.common.property.ActiveMqProperties;
import se.inera.intyg.intygsbestallning.common.property.MailSenderProperties;

@EnableJms
@Configuration
@ComponentScan(basePackages = {"se.inera.intyg.intygsbestallning.mailsender", "se.inera.intyg.intygsbestallning.common.property"})
public class MailSenderConfig extends CamelConfiguration {

    private final MailSenderProperties mailSenderProperties;
    private final ActiveMqProperties activeMqProperties;

    public MailSenderConfig(
            MailSenderProperties mailSenderProperties,
            ActiveMqProperties activeMqProperties) {
        this.mailSenderProperties = mailSenderProperties;
        this.activeMqProperties = activeMqProperties;
    }

    @Bean
    public SpringTransactionPolicy myTxPolicy() {
        return new SpringTransactionPolicy(new JmsTransactionManager(connectionFactory()));
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(
                activeMqProperties.getUsername(),
                activeMqProperties.getPassword(),
                activeMqProperties.getUrl());
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setDefaultDestinationName(mailSenderProperties.getQueueName());
        return jmsTemplate;
    }
}
