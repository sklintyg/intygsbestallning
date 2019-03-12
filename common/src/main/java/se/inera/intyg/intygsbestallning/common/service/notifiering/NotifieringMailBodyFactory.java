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
import se.inera.intyg.intygsbestallning.common.property.MailProperties;

@Component
public class NotifieringMailBodyFactory implements MailBodyFactory {

    private MailProperties mailProperties;

    private STGroup templateGroup = new STGroupFile("notifiering-templates/notifiering.stg");

    public NotifieringMailBodyFactory(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    public String buildBodyForUtredning(String message, String url) {
        ST utredningTemplate = templateGroup.getInstanceOf("mail");
        utredningTemplate.add("data", new MailContent(mailProperties.getHost(), message, "utredningen", url));
        return utredningTemplate.render();
    }

    public String buildBodyForForfragan(String message, String url) {
        ST forfraganTemplate = templateGroup.getInstanceOf("mail");
        forfraganTemplate.add("data", new MailContent(mailProperties.getHost(), message, "förfrågan", url));
        return forfraganTemplate.render();
    }

    private static final class MailContent {
        private String hostUrl;
        private String message;
        private String linkType;
        private String url;

        MailContent(String hostUrl, String message, String linkType, String url) {
            this.hostUrl = hostUrl;
            this.message = message;
            this.linkType = linkType;
            this.url = url;
        }
    }
}
