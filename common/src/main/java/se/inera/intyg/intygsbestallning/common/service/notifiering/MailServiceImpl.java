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

package se.inera.intyg.intygsbestallning.common.service.notifiering;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.common.exception.IbServiceException;
import se.inera.intyg.intygsbestallning.common.json.CustomObjectMapper;
import se.inera.intyg.intygsbestallning.common.mail.NotificationEmail;

/**
 * Puts an ObjectMessage representing the email on a queue, that will be picked up by the mail-sender Camel route.
 */
@Service
public class MailServiceImpl implements MailService {

    private ObjectMapper objectMapper = new CustomObjectMapper();

    private final JmsTemplate jmsTemplate;

    public MailServiceImpl(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void sendNotifieringToVardenhet(String mailAddress, String subject, String body) {
        jmsTemplate.send(session -> {
            try {
                return session
                        .createTextMessage(objectMapper.writeValueAsString(new NotificationEmail(mailAddress, subject, body)));
            } catch (JsonProcessingException e) {
                throw new IbServiceException(IbErrorCodeEnum.BESTALLNING_FEL004_TEKNISKT_FEL,
                        "Unable to marshal notification message, reason: " + e.getMessage());
            }
        });
    }
}
