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
package se.inera.intyg.intygsbestallning.web.pdl;

import se.inera.intyg.infra.logmessages.ActivityType;

public enum PdlLogEvent {

    BESTALLNING_OPPNAS_OCH_LASES("Beställning läst", ActivityType.READ),
    BESTALLNING_ACCEPTERAS("Beställning accepterad", ActivityType.CREATE),
    BESTALLNING_AVVISAS("Beställning avvisad", ActivityType.UPDATE),
    BESTALLNING_KLARMARKERAS("Beställning klarmarkerad", ActivityType.UPDATE),
    BESTALLNING_SKRIV_UT("Beställning utskriven", ActivityType.PRINT),
    PERSONINFORMATION_VISAS_I_LISTA("Visad i lista", ActivityType.READ);

    private final String id;
    private final String activityArgs;
    private final ActivityType activityType;

    PdlLogEvent(String activityArgs, ActivityType activityType) {
        this.id = this.name();
        this.activityArgs = activityArgs;
        this.activityType = activityType;
    }

    public String getId() {
        return id;
    }

    public String getActivityArgs() {
        return activityArgs;
    }

    public ActivityType getActivityType() {
        return activityType;
    }
}
