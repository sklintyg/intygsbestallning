package se.inera.intyg.intygsbestallning.common.service.notifiering;

import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;
import se.inera.intyg.intygsbestallning.common.mail.MailContent;

public interface MailTextService {
    MailContent getMailContent(NotifieringTyp key);
}
