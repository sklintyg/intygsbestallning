package se.inera.intyg.intygsbestallning.common.service.notifiering;

import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;

/**
 * Used for sending notifications by email to vardenhet users. Notifications will include a deep link to the related
 * se.inera.intyg.intygsbestallning.common.domain.Bestallning so that the user can quickly enter the application at the screen for the se.inera.intyg.intygsbestallning.common.domain.Bestallning of interest.
 */
public interface NotifieringSendService {

    /**
     * Send deep link to specific se.inera.intyg.intygsbestallning.common.domain.Bestallning to user at vardenhet when new intygsbestallning arrives.
     *
     * @param bestallning
     */
    void nyBestallning(Bestallning bestallning);

    /**
     * Send deep link to specific Bestallning to user at vardenhet when intygsbestallning is forwarded (vidarebefodrad).
     * Note: notification is sent to the vardenhet receiving the forwarded intygsbestallning.
     *
     * @param bestallning
     * @return
     */
    String vidarebefordrad(Bestallning bestallning);

    void paminnelse(Bestallning bestallning, NotifieringTyp typ);
}
