package se.inera.intyg.intygsbestallning.web.notifiering;

import se.inera.intyg.intygsbestallning.common.Utredning;

/**
 * Used for sending notifications by email to vardenhet users. Notifications will include a deep link to the related
 * Utredning so that the user can quickly enter the application at the screen for the Utredning of interest.
 */
public interface NotifieringSendService {

    /**
     * Send deep link to specific Utredning to user at vardenhet when new intygsbestallning arrives.
     *
     * @param utredning
     */
    public void notifieraVardenhetsAnvandareNyIntygsbestallning(Utredning utredning);

    /**
     * Send deep link to specific Utredning to user at vardenhet when intygsbestallning is forwarded (vidarebefodrad).
     * Note: notification is sent to the vardenhet receiving the forwarded intygsbestallning.
     *
     * @param utredning
     */
    public void notifieraAnvandareHosVidarebefodradVardenhet(Utredning utredning);
}
