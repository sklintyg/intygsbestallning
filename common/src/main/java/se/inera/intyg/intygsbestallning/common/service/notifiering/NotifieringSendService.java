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

import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;
import se.inera.intyg.intygsbestallning.common.mail.NotificationEmail;

/**
 * Used for sending notifications by email to vardenhet users. Notifications will include a deep link to the related
 * se.inera.intyg.intygsbestallning.common.domain.Bestallning so that the user can quickly enter the application at the screen for the
 * se.inera.intyg.intygsbestallning.common.domain.Bestallning of interest.
 */
public interface NotifieringSendService {

    /**
     * Send deep link to specific se.inera.intyg.intygsbestallning.common.domain.Bestallning
     * to user at vardenhet when new intygsbestallning arrives.
     *
     * @param bestallning - the current bestallning
     */
    void nyBestallning(Bestallning bestallning);

    /**
     * Send deep link to specific Bestallning to user at vardenhet when intygsbestallning is forwarded (vidarebefodrad).
     * Note: notification is sent to the vardenhet receiving the forwarded intygsbestallning.
     *
     * @param bestallning - the current bestallning
     * @return String containing the mail-text
     */
    NotificationEmail vidarebefordrad(Bestallning bestallning);

    void paminnelse(Bestallning bestallning, NotifieringTyp typ);
}
