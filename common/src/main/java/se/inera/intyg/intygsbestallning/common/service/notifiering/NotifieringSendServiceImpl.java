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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;

@Service
public class NotifieringSendServiceImpl implements NotifieringSendService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private MailService mailService;
    private MailTextService mailTextService;

    public NotifieringSendServiceImpl(MailService mailService, MailTextService mailTextService) {
        this.mailService = mailService;
        this.mailTextService = mailTextService;
    }

    @Override
    public void nyBestallning(Bestallning bestallning) {
        var nyBestallning = NotifieringTyp.NY_BESTALLNING;
        var mailContent = mailTextService.getMailContent(nyBestallning, bestallning.getIntygTyp());
    }

    @Override
    public void vidarebefordrad(Bestallning bestallning) {
        var nyBestallning = NotifieringTyp.NY_BESTALLNING;
        var mailContent = mailTextService.getMailContent(nyBestallning, bestallning.getIntygTyp());
    }

    private void sendNotifiering(String email, String subject, String body, Long bestallningId) {
        try {
            mailService.sendNotifieringToVardenhet(email, subject, body);
            LOG.info(MessageFormat.format("Sent notification: \"{0}\" to email: {1} for bestalling: {2}.", subject, email, bestallningId));
        } catch (MessagingException e) {
            LOG.error(MessageFormat.format("Error sending notification by email: {0}", e.getMessage()));
        }
    }
}
