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
package se.inera.intyg.intygsbestallning.web.service.notifiering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import se.inera.intyg.intygsbestallning.common.domain.Utredning;
import se.inera.intyg.intygsbestallning.common.service.mail.MailService;

@Service
public class NotifieringSendServiceImpl implements NotifieringSendService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private MailService mailService;

    public NotifieringSendServiceImpl(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void notifieraVardenhetsAnvandareNyIntygsbestallning(Utredning utredning) {
        //TODO: Below.
        //String templating, create emailbody, yadayada
        //Send notifiering
        //Persist notifiering
    }

    @Override
    public void notifieraAnvandareHosVidarebefodradVardenhet(Utredning utredning) {
        //final String vardenhetsHsaId = utredning.getVardenhet().getHsaId();
        //final NotificationPreferenceDto preferens = notifieringPreferenceService.getNotificationPreference(vardenhetsHsaId, VE);

        //TODO: Below.
        //String templating, create emailbody, yadayada
        //Send notifiering
        //Persist notifiering
    }

    private void sendNotifiering(String email, String subject, String body, Long utredningId) {
        try {
            mailService.sendNotificationToUnit(email, subject, body);
            LOG.info(MessageFormat.format("Sent notification: \"{0}\" to email: {1} for utredning: {2}.", subject, email, utredningId));
        } catch (MessagingException e) {
            LOG.error(MessageFormat.format("Error sending notification by email: {0}", e.getMessage()));
        }
    }
}