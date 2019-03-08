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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.inera.intyg.infra.logmessages.ActivityPurpose;
import se.inera.intyg.infra.logmessages.ActivityType;
import se.inera.intyg.infra.logmessages.Enhet;
import se.inera.intyg.infra.logmessages.Patient;
import se.inera.intyg.infra.logmessages.PdlLogMessage;
import se.inera.intyg.infra.logmessages.PdlResource;
import se.inera.intyg.intygsbestallning.web.pdl.LogMessage;
import se.inera.intyg.intygsbestallning.web.pdl.LogUser;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.auth.IbSelectableHsaEntity;
import se.inera.intyg.intygsbestallning.web.auth.IbSelectableHsaEntityType;
import se.inera.intyg.intygsbestallning.web.auth.IbUser;
import se.inera.intyg.intygsbestallning.web.auth.IbVardenhet;
import se.inera.intyg.intygsbestallning.web.auth.IbVardgivare;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdlLogMessageFactoryImpl implements PdlLogMessageFactory {

    @Value("${pdlLogging.systemId}")
    private String systemId;

    @Value("${pdlLogging.systemName}")
    private String systemName;

    @Override
    public PdlLogMessage buildLogMessage(LogMessage logMessage,
                                         IbUser ibUser) {

        LogUser logUser = getLogUser(ibUser);
        LogActivity logActivity = logMessage.getActivity();

        PdlLogMessage pdlLogMessage = getPdlLogMessage(logActivity);
        pdlLogMessage.setActivityArgs(logActivity.getEvent().getActivityArgs());
        pdlLogMessage.setActivityLevel(logActivity.getActivityLevel());

        populateWithCurrentUserAndCareUnit(pdlLogMessage, logUser);

        // Add resources
        pdlLogMessage.getPdlResourceList().addAll(
                logMessage.getResources().stream()
                        .map(logResource -> buildPdlLogResource(logResource, logUser))
                        .collect(Collectors.toList()));
        return pdlLogMessage;
    }

    private PdlLogMessage getPdlLogMessage(LogActivity logActivity) {
        PdlLogMessage pdlLogMessage = new PdlLogMessage(
                logActivity.getEvent().getActivityType(),
                logActivity.getPurpose());

        pdlLogMessage.setSystemId(systemId);
        pdlLogMessage.setSystemName(systemName);

        return pdlLogMessage;
    }

    private LogUser getLogUser(IbUser ibUser) {
        IbSelectableHsaEntity loggedInAt = ibUser.getCurrentlyLoggedInAt();

        if (loggedInAt.type() == IbSelectableHsaEntityType.VE) {
            IbVardenhet ve = (IbVardenhet) loggedInAt;
            LogUser logUser = new LogUser(ibUser.getHsaId(), ve.getId(), ve.getParent().getId());
            logUser.setUserName(ibUser.getNamn());
            logUser.setUserAssignment(ibUser.getSelectedMedarbetarUppdragNamn());
            logUser.setUserTitle(ibUser.getTitel());
            logUser.setEnhetsNamn(ve.getName());
            logUser.setVardgivareNamn(ve.getParent().getName());
            return logUser;
        } else {
            throw new IllegalStateException("There cannot be any PDL logging for Samordnare given that they should never "
                    + "see any PDL-eligible information.");
        }
    }

    private void populateWithCurrentUserAndCareUnit(PdlLogMessage pdlLogMsg, LogUser logUser) {
        pdlLogMsg.setUserId(logUser.getUserId());
        pdlLogMsg.setUserName(logUser.getUserName());
        pdlLogMsg.setUserAssignment(logUser.getUserAssignment());
        pdlLogMsg.setUserTitle(logUser.getUserTitle());

        Enhet vardenhet = new Enhet(logUser.getEnhetsId(), logUser.getEnhetsNamn(), logUser.getVardgivareId(), logUser.getVardgivareNamn());
        pdlLogMsg.setUserCareUnit(vardenhet);
    }

    private Patient getPatient(LogResource logResource) {
        return new Patient(
                logResource.getPatientId().replace("-", "").replace("+", ""),
                "");
    }

    private PdlResource buildPdlLogResource(LogResource logResource, LogUser logUser) {
        PdlResource pdlResource = new PdlResource();
        pdlResource.setPatient(getPatient(logResource));
        pdlResource.setResourceOwner(
                new Enhet(logUser.getEnhetsId(), logUser.getEnhetsNamn(), logUser.getVardgivareId(), logUser.getVardgivareNamn()));
        pdlResource.setResourceType(logResource.getResourceType().getResourceTypeName());

        return pdlResource;
    }

}
