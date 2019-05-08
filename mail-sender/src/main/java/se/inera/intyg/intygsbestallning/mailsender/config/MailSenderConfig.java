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

import java.lang.invoke.MethodHandles;
import java.util.Properties;
import com.google.common.primitives.Ints;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import se.inera.intyg.intygsbestallning.common.property.ActiveMqProperties;
import se.inera.intyg.intygsbestallning.common.property.MailProperties;
import se.inera.intyg.intygsbestallning.common.property.MailSenderProperties;

import javax.jms.ConnectionFactory;

@EnableJms
@Configuration
@ComponentScan(basePackages = {
        "org.springframework.mail.javamail",
        "se.inera.intyg.intygsbestallning.common",
        "se.inera.intyg.intygsbestallning.mailsender"})
public class MailSenderConfig extends CamelConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private final ActiveMqProperties activeMqProperties;
    private final MailSenderProperties mailSenderProperties;
    private final MailProperties mailProperties;

    public MailSenderConfig(
            ActiveMqProperties activeMqProperties,
            MailSenderProperties mailSenderProperties,
            MailProperties mailProperties) {
        this.activeMqProperties = activeMqProperties;
        this.mailSenderProperties = mailSenderProperties;
        this.mailProperties = mailProperties;
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

    @Bean
    @Profile(value = {"demo", "qa", "prod"})
    public JavaMailSender mailSender() {
        var mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setDefaultEncoding(mailProperties.getDefaultEncoding());
        mailSender.setProtocol(mailProperties.getProtocol());
        mailSender.setPort(Ints.tryParse(mailProperties.getPort()));

        if (Strings.isNotEmpty(mailProperties.getUsername())) {
            mailSender.setUsername(mailProperties.getUsername());
        }
        if (Strings.isNotEmpty(mailProperties.getPassword())) {
            mailSender.setPassword(mailProperties.getPassword());
        }

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail." + mailProperties.getProtocol() + ".auth", mailProperties.getSmtpsAuth());
        javaMailProperties.put("mail." + mailProperties.getProtocol() + ".port", mailProperties.getHost());
        javaMailProperties.put("mail." + mailProperties.getProtocol() + ".starttls.enable", mailProperties.getSmtpsStarttlsEnable());
        javaMailProperties.put("mail." + mailProperties.getProtocol() + ".debug", mailProperties.getSmtpsDebug());
        javaMailProperties.put("mail." + mailProperties.getProtocol() + ".socketFactory.fallback", true);

        mailSender.setJavaMailProperties(javaMailProperties);
        LOG.info("Mailsender initialized with: [port: {}, protocol: {}, host: {}]",
                mailProperties.getPort(), mailProperties.getProtocol(), mailProperties.getHost());
        return mailSender;
    }
}
