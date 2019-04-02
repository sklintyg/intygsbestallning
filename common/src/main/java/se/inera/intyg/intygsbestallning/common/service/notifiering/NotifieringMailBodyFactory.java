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
package se.inera.intyg.intygsbestallning.common.service.notifiering;

import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.mail.MailContent;
import se.inera.intyg.intygsbestallning.common.property.MailProperties;
import se.inera.intyg.intygsbestallning.common.text.mail.MailTexter;

@Component
public class NotifieringMailBodyFactory implements MailBodyFactory {

    private MailProperties mailProperties;

    private STGroup templateGroup = new STGroupFile("notifiering-templates/notifiering.stg");

    public NotifieringMailBodyFactory(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    public String buildBody(Bestallning bestallning, MailTexter texter, String url) {

        ST utredningTemplate = templateGroup.getInstanceOf("mail");
        enrichInkomstDatum(bestallning, texter);
        utredningTemplate.add("data", new MailContent(mailProperties.getHost(), texter, url));

        return utredningTemplate.render();
    }

    private void enrichInkomstDatum(Bestallning bestallning, MailTexter mailTexter) {
        var daysAgo = ChronoUnit.DAYS.between(bestallning.getAnkomstDatum(), LocalDate.now());
        var replaced = mailTexter.getBody().getText1().replace("{0}", Long.toString(daysAgo));

        mailTexter.getBody().setText1(replaced);
    }
}
