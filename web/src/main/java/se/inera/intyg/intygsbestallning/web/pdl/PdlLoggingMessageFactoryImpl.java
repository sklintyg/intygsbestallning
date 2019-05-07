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

import org.springframework.stereotype.Service;
import se.inera.intyg.infra.logmessages.Enhet;
import se.inera.intyg.infra.logmessages.Patient;
import se.inera.intyg.infra.logmessages.PdlLogMessage;
import se.inera.intyg.infra.logmessages.PdlResource;
import se.inera.intyg.intygsbestallning.common.property.PdlLoggingProperties;
import se.inera.intyg.intygsbestallning.web.auth.IbSelectableHsaEntity;
import se.inera.intyg.intygsbestallning.web.auth.IbSelectableHsaEntityType;
import se.inera.intyg.intygsbestallning.web.auth.IbVardenhet;
import se.inera.intyg.intygsbestallning.web.auth.IntygsbestallningUser;

import java.util.stream.Collectors;

@Service
public class PdlLoggingMessageFactoryImpl implements PdlLoggingMessageFactory {

    private PdlLoggingProperties pdlLoggingProperties;

    public PdlLoggingMessageFactoryImpl(PdlLoggingProperties pdlLoggingProperties) {
        this.pdlLoggingProperties = pdlLoggingProperties;
    }

    @Override
    public PdlLogMessage buildLogMessage(LogMessage logMessage,
                                         IntygsbestallningUser ibUser) {

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

        // Unset values due to regulations
        unsetValues(pdlLogMessage);

        return pdlLogMessage;
    }

    private PdlResource buildPdlLogResource(LogResource logResource, LogUser logUser) {
        PdlResource pdlResource = new PdlResource();
        pdlResource.setPatient(getPatient(logResource));
        pdlResource.setResourceOwner(getCareUnit(logUser));
        pdlResource.setResourceType(logResource.getResourceType().getResourceTypeName());

        return pdlResource;
    }

    private Enhet createCareUnit(String enhetsId, String enhetsNamn, String vardgivareId, String vardgivareNamn) {
        return new Enhet(enhetsId, enhetsNamn, vardgivareId, vardgivareNamn);
    }

    private Patient createPatient(String patientId, String patientName) {
        return new Patient(patientId, patientName);
    }

    private Enhet getCareUnit(LogUser logUser) {
        return createCareUnit(logUser.getEnhetsId(), logUser.getEnhetsNamn(), logUser.getVardgivareId(), logUser.getVardgivareNamn());
    }

    private LogUser getLogUser(IntygsbestallningUser ibUser) {
        IbSelectableHsaEntity loggedInAt = ibUser.getUnitContext();

        if (loggedInAt != null && loggedInAt.type() == IbSelectableHsaEntityType.VE) {
            IbVardenhet ve = (IbVardenhet) loggedInAt;
            LogUser logUser = new LogUser(ibUser.getHsaId(), ve.getId(), ve.getParentHsaId());
            logUser.setUserAssignment(ibUser.getSelectedMedarbetarUppdragNamn());
            logUser.setUserTitle(ibUser.getTitel());
            logUser.setEnhetsNamn(ve.getName());
            logUser.setVardgivareNamn(ve.getParentHsaName());
            return logUser;
        } else {
            // TODO: Make better error message
            throw new IllegalStateException("There cannot be any PDL logging for Vårdadministratör given that they should never "
                    + "see any PDL-eligible information.");
        }
    }

    private PdlLogMessage getPdlLogMessage(LogActivity logActivity) {
        PdlLogMessage pdlLogMessage = new PdlLogMessage(
                logActivity.getEvent().getActivityType(),
                logActivity.getPurpose());

        pdlLogMessage.setSystemId(pdlLoggingProperties.getSystemId());
        pdlLogMessage.setSystemName(pdlLoggingProperties.getSystemName());

        return pdlLogMessage;
    }

    private void populateWithCurrentUserAndCareUnit(PdlLogMessage pdlLogMsg, LogUser logUser) {
        pdlLogMsg.setUserId(logUser.getUserId());
        pdlLogMsg.setUserName(logUser.getUserName());
        pdlLogMsg.setUserAssignment(logUser.getUserAssignment());
        pdlLogMsg.setUserTitle(logUser.getUserTitle());

        Enhet vardenhet = getCareUnit(logUser);
        pdlLogMsg.setUserCareUnit(vardenhet);
    }

    private Patient getPatient(LogResource logResource) {
        return createPatient(
                logResource.getPatientId().replace("-", "").replace("+", ""),
                logResource.getPatientNamn());
    }

    private void unsetValues(final PdlLogMessage logMessage) {
        // Inget användarnamn vid PDL-logging
        logMessage.setUserName("");

        // Inget patientnamn vid PDL-logging
        logMessage.getPdlResourceList().forEach(pdlResource ->
                pdlResource.setPatient(createPatient(pdlResource.getPatient().getPatientId(), "")));
    }

}