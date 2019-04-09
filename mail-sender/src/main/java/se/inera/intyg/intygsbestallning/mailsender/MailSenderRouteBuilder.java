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
package se.inera.intyg.intygsbestallning.mailsender;

import org.apache.camel.LoggingLevel;
import org.apache.camel.processor.RedeliveryPolicy;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import se.inera.intyg.intygsbestallning.common.property.MailSenderProperties;
import se.inera.intyg.intygsbestallning.mailsender.exception.TemporaryException;
import se.inera.intyg.intygsbestallning.mailsender.service.MailSender;

@Component
public class MailSenderRouteBuilder extends SpringRouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(MailSenderRouteBuilder.class);

    private static final String JMS_QUEUE_PREFIX = "jms:queue:";
    private static final String MY_TX_POLICY = "myTxPolicy";

    private MailSenderProperties mailSenderProperties;
    private MailSender ibMailSender;

    public MailSenderRouteBuilder(
            MailSenderProperties mailSenderProperties,
            MailSender ibMailSender) {
        this.mailSenderProperties = mailSenderProperties;
        this.ibMailSender = ibMailSender;
    }

    @Override
    public void configure() {

        final RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setLogContinued(true);
        redeliveryPolicy.setLogHandled(true);
        redeliveryPolicy.setLogRetryAttempted(true);
        redeliveryPolicy.setLogNewException(true);
        redeliveryPolicy.setMaximumRedeliveries(mailSenderProperties.getMaximumRedeliveries());
        redeliveryPolicy.setRedeliveryDelay(mailSenderProperties.getRedeliveryDelay());
        redeliveryPolicy.setBackOffMultiplier(mailSenderProperties.getBackOffMultiplier());
        redeliveryPolicy.setUseExponentialBackOff(true);
        redeliveryPolicy.setMaximumRedeliveryDelay(-1L);

        from(JMS_QUEUE_PREFIX + mailSenderProperties.getQueueName()).routeId("mailSenderRoute")
                .onException(TemporaryException.class).redeliveryPolicy(redeliveryPolicy)
                .maximumRedeliveries(mailSenderProperties.getMaximumRedeliveries()).to("direct:temporaryErrorHandlerEndpoint").end()
                .onException(Exception.class).handled(true).to("direct:permanentErrorHandlerEndpoint").end()
                .transacted()
//                .transacted(MY_TX_POLICY)
                .bean(ibMailSender, "process")
                .end();

        from("direct:permanentErrorHandlerEndpoint").routeId("errorLogging")
                .log(LoggingLevel.ERROR, LOG,
                        simple("Permanent exception, with message: "
                                + "${exception.message}\n ${exception.stacktrace}")
                                        .getText())
                .stop();

        from("direct:temporaryErrorHandlerEndpoint").routeId("temporaryErrorLogging")
                .choice()
                .when(header("JMSRedelivered").isEqualTo("false"))
                .log(LoggingLevel.ERROR, LOG,
                        simple("Temporary exception, with message: "
                                + "${exception.message}\n ${exception.stacktrace}")
                                        .getText())
                .otherwise()
                .log(LoggingLevel.WARN, LOG,
                        simple("Temporary exception"
                                + ", with message: "
                                + "${exception.message}").getText())
                .stop();
    }
}
