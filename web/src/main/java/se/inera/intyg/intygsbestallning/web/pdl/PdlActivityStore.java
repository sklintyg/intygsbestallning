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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import se.inera.intyg.infra.logmessages.ActivityType;
import se.inera.intyg.infra.logmessages.ResourceType;

/**
 * A simple Map based store to cache combinations of vardenhet/patient/ActivityType
 * Created by marced on 22/02/16.
 */
public final class PdlActivityStore {

    private PdlActivityStore() {
    }

    /**
     * Should return list of bestallningar (internally identified by patient) not already present in store
     * for this vardenhet, activityType and resourceType.
     */
    public static List<? extends LogActivity> getActivitiesNotInStore(
            String patientId,
            String enhetsId,
            ActivityType activityType,
            ResourceType resourceType,
            List<LogActivity> activities,
            Map<String, List<PdlActivityEntry>> storedActivities) {

        if (activities == null || activities.isEmpty()) {
            return new ArrayList<>();
        }

        if (storedActivities == null || storedActivities.isEmpty()) {
            return activities;
        }

        // We actually don't check tha vardenehet for each sjukfall, we trust that the given enhetsId is correct.
        List<PdlActivityEntry> vardenhetEvents = storedActivities.get(enhetsId);

        if (vardenhetEvents == null) {
            // Nothing logged for this vardenhet yet - so all are new activities
            return activities;
        }

        // find all patientId's NOT having av event entry for the combination patientId + eventType
        return activities.stream()
                .filter(activity -> vardenhetEvents.stream()
                        .noneMatch(storedEvent -> isStoredEvent(storedEvent, patientId, activityType, resourceType)))
                .collect(Collectors.toList());

    }

    /**
     * Should store the specified sjukfall for the vardenhet and activityType.
     *
     * @param enhetsId         - enhetsId
     * @param activitiesToAdd  - activitiesToAdd
     * @param activityType     - activityType
     * @param storedActivities - storedActivities
     */
    public static void addActivitiesToStore(
            String patientId,
            String enhetsId,
            ActivityType activityType,
            ResourceType resourceType,
            List<LogActivity> activitiesToAdd,
            Map<String, List<PdlActivityEntry>> storedActivities) {

        if (activitiesToAdd == null || activitiesToAdd.isEmpty()) {
            return;
        }

        List<PdlActivityEntry> vardenhetEvents = storedActivities.get(enhetsId);

        final List<PdlActivityEntry> pdlActivityEntryList = activitiesToAdd.stream()
                .map(sf -> new PdlActivityEntry(patientId, activityType, resourceType))
                .collect(Collectors.toList());

        if (vardenhetEvents == null) {
            storedActivities.put(enhetsId, pdlActivityEntryList);
        } else {
            vardenhetEvents.addAll(pdlActivityEntryList);
        }
    }

    private static boolean isStoredEvent(PdlActivityEntry storedEvent, String patientId,
                                         ActivityType activityType, ResourceType resourceType) {

        return storedEvent.getPatientId().equals(patientId)
                && storedEvent.getActivityType().equals(activityType)
                && storedEvent.getResourceType().equals(resourceType);
    }

}
