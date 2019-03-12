package se.inera.intyg.intygsbestallning.common.service.mail;

import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;
import se.inera.intyg.intygsbestallning.common.mail.MailContent;

public interface MailTextService {
    MailContent getMailContent(NotifieringTyp key);
}
