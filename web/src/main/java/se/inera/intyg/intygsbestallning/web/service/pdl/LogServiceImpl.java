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

package se.inera.intyg.intygsbestallning.web.service.pdl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import se.inera.intyg.infra.logmessages.PdlLogMessage;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.common.exception.IbJMSException;
import se.inera.intyg.intygsbestallning.common.json.CustomObjectMapper;
import se.inera.intyg.intygsbestallning.web.pdl.LogActivity;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.pdl.LogMessage;
import se.inera.intyg.intygsbestallning.web.pdl.LogResource;
import se.inera.intyg.intygsbestallning.web.pdl.PdlLoggingMessageFactory;
import se.inera.intyg.intygsbestallning.web.service.user.UserService;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Implementation of service for logging user actions according to PDL requirements.
 *
 * @author eriklupander
 */
@Service
public class LogServiceImpl implements LogService {

    private static final Logger LOG = LoggerFactory.getLogger(LogServiceImpl.class);

    private static final String ACTIVITY_LEVEL = "Översikt";

    @Autowired(required = false)
    @Qualifier("jmsPdlLoggingTemplate")
    private JmsTemplate jmsTemplate;

    @Autowired
    private PdlLoggingMessageFactory pdlLoggingMessageFactory;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void checkJmsTemplate() {
        if (jmsTemplate == null) {
            LOG.error("INTYGSBESTALLNING: PDL-logging is disabled!");
        }
    }

    @Override
    public void log(final String bestallningId, final String patientId,
                    final String enhetsId, final String vardgivareId, final LogEvent logEvent) {

        String activityLevel = logEvent.equals(LogEvent.PERSONINFORMATION_VISAS_I_LISTA) ? ACTIVITY_LEVEL : bestallningId;

        LogActivity logActivity = new LogActivity(activityLevel, logEvent);
        LogResource logResource = new LogResource(patientId, enhetsId, vardgivareId);
        LogMessage logMessage = new LogMessage(logActivity, ImmutableList.of(logResource));

        PdlLogMessage pdlLogMessage = pdlLoggingMessageFactory.buildLogMessage(logMessage, userService.getUser());
        send(pdlLogMessage);
    }

    @Override
    public void log(Bestallning bestallning, LogEvent logEvent) {
        if (bestallning == null) {
            LOG.debug("INTYGSBESTALLNING: Argument bestallning was null - no PDL-logging.");
            return;
        }

        log(bestallning.getId().toString(),
            bestallning.getInvanare().getPersonId().getPersonnummer(),
            bestallning.getVardenhet().getHsaId(),
            bestallning.getVardenhet().getVardgivareHsaId(),
            logEvent);
    }

    @Override
    public void logList(List<? extends Bestallning> bestallningListItems, LogEvent logEvent) {
        if (bestallningListItems == null) {
            LOG.debug("INTYGSBESTALLNING: Argument bestallningListItems was null - no PDL-logging.");
            return;
        }

        bestallningListItems.stream()
                .filter(distinctByKey(b -> b.getInvanare().getPersonId().getPersonnummer()))
                .forEach(b -> log(b, logEvent));
    }

    void send(PdlLogMessage pdlLogMessage) {
        LOG.info("INTYGSBESTALLNING: PDL-logging for activity '{}' by user {}",
                pdlLogMessage.getActivityArgs(),
                pdlLogMessage.getUserId());

        try {
            jmsTemplate.send(new MC(pdlLogMessage));
        } catch (JmsException e) {
            LOG.error("INTYGSBESTALLNING: Unsuccessful to perform PDL-logging", e);
            throw new IbJMSException(IbErrorCodeEnum.UNKNOWN_INTERNAL_PROBLEM,
                    "Error connecting to JMS broker when performing PDL-logging. This is probably a configuration error.");
        }
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        final Set<Object> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private static final class MC implements MessageCreator {
        private final PdlLogMessage logMsg;
        private final ObjectMapper objectMapper = new CustomObjectMapper();

        private MC(PdlLogMessage logMsg) {
            this.logMsg = logMsg;
        }

        @Override
        public Message createMessage(Session session) throws JMSException {
            try {
                return session.createTextMessage(objectMapper.writeValueAsString(this.logMsg));
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException(
                        "INTYGSBESTALLNING: Could not serialize log message of type '"
                                + logMsg.getClass().getName()
                                + "' into JSON, message: " + e.getMessage(), e);
            }
        }
    }
}