package se.inera.intyg.intygsbestallning.common.service.notifiering;

import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;
import se.inera.intyg.intygsbestallning.common.text.mail.MailTexter;

public interface MailTextService {
    MailTexter getMailContent(NotifieringTyp key, String intygTyp);
}
