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
package se.inera.intyg.intygsbestallning.web.service.bestallning;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.List;
import se.inera.intyg.infra.integration.hsa.exception.HsaServiceCallException;
import se.inera.intyg.infra.integration.hsa.services.HsaOrganizationsService;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationErrorCode;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationException;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.common.service.notifiering.NotifieringSendService;
import se.inera.intyg.intygsbestallning.integration.pu.PatientService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.persistence.service.InvanarePersistenceService;
import se.inera.intyg.intygsbestallning.persistence.service.VardenhetPersistenceService;

@Service
@Transactional
public class CreateBestallningServiceImpl implements CreateBestallningService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private BestallningPersistenceService bestallningPersistenceService;
    private InvanarePersistenceService invanarePersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private VardenhetPersistenceService vardenhetPersistenceService;
    private NotifieringSendService notifieringSendService;
    private PatientService patientService;
    private HsaOrganizationsService hsaOrganizationsService;

    public CreateBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            InvanarePersistenceService invanarePersistenceService,
            BestallningStatusResolver bestallningStatusResolver,
            VardenhetPersistenceService vardenhetPersistenceService,
            NotifieringSendService notifieringSendService,
            PatientService patientService,
            HsaOrganizationsService hsaOrganizationsService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.invanarePersistenceService = invanarePersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.vardenhetPersistenceService = vardenhetPersistenceService;
        this.notifieringSendService = notifieringSendService;
        this.patientService = patientService;
        this.hsaOrganizationsService = hsaOrganizationsService;
    }

    @Override
    public Long create(CreateBestallningRequest createBestallningRequest) {

        LOG.debug("Creating new bestallning");

        var existing = invanarePersistenceService.getInvanareByPersonnummer(createBestallningRequest.getInvanare().getPersonnummer());

        Invanare invanare;
        if (existing.isEmpty()) {
            var person = Try.of(() -> patientService.lookupPersonnummerFromPU(createBestallningRequest.getInvanare().getPersonnummer()));

            if (person.isFailure()) {
                throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL10, List.of());
            }

            if (person.get().isEmpty()) {
                throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL11, List.of());
            }

            var foundPerson = person.get().get();

            invanare = Invanare.Factory.newInvanare(
                    foundPerson.getPersonnummer(), createBestallningRequest.getInvanare().getBakgrundNulage());

        } else {
            invanare = existing.get();
            invanare.setBakgrundNulage(createBestallningRequest.getInvanare().getBakgrundNulage());
        }

        var vardenhetRespons = Try.of(() -> hsaOrganizationsService.getVardenhet(createBestallningRequest.getVardenhet()));

        if (vardenhetRespons.isFailure() && vardenhetRespons.getCause() instanceof HsaServiceCallException) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL03, List.of());
        } else if (vardenhetRespons.isFailure()) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL04, List.of());
        }

        if (vardenhetRespons.get().getEpost() == null) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL09, List.of());
        }

        var vardGivareRespons = hsaOrganizationsService.getVardgivareOfVardenhet(createBestallningRequest.getVardenhet());

        if (vardGivareRespons == null) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL03, List.of());
        }

        if (vardGivareRespons.getId() == null) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL04, List.of());
        }

        if (vardGivareRespons.getOrgId() == null) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL06, List.of());
        }

        var existingVardenhet = vardenhetPersistenceService.getVardenhetByHsaId(vardenhetRespons.get().getId());


        Vardenhet vardenhet;
        if (existingVardenhet.isPresent()) {
            vardenhet = existingVardenhet.get();
            vardenhet.setHsaId(vardenhetRespons.get().getId());
            vardenhet.setVardgivareHsaId(vardGivareRespons.getId());
            vardenhet.setNamn(vardenhetRespons.get().getNamn());
            vardenhet.setEpost(vardenhetRespons.get().getEpost());
        } else {
            vardenhet = Vardenhet.Factory.newVardenhet(
                    vardenhetRespons.get().getId(),
                    vardGivareRespons.getId(),
                    vardGivareRespons.getOrgId(),
                    vardenhetRespons.get().getNamn(),
                    vardenhetRespons.get().getEpost(),
                    null);
        }

        if (vardenhet.getEpost() == null) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL09, List.of());
        }

        var handlaggare = Handlaggare.Factory.newHandlaggare(
                createBestallningRequest.getHandlaggare().getNamn(),
                createBestallningRequest.getHandlaggare().getTelefonNummer(),
                createBestallningRequest.getHandlaggare().getEmail(),
                createBestallningRequest.getHandlaggare().getMyndighet(),
                createBestallningRequest.getHandlaggare().getKontor().getNamn(),
                createBestallningRequest.getHandlaggare().getKontor().getKostnadsStalle(),
                createBestallningRequest.getHandlaggare().getKontor().getPostAdress(),
                createBestallningRequest.getHandlaggare().getKontor().getPostKod(),
                createBestallningRequest.getHandlaggare().getKontor().getPostOrt());

        var bestallning = Bestallning.Factory.newBestallning(
                invanare,
                createBestallningRequest.getSyfte(),
                createBestallningRequest.getPlaneradeInsatser(),
                handlaggare,
                createBestallningRequest.getIntygTyp(),
                createBestallningRequest.getIntygVersion(),
                vardenhet,
                createBestallningRequest.getArendeReferens());

        bestallningStatusResolver.setStatus(bestallning);

        final Bestallning savedBestallning = bestallningPersistenceService.saveNewBestallning(bestallning);
        notifieringSendService.nyBestallning(savedBestallning);
        bestallningPersistenceService.updateBestallning(savedBestallning);

        return savedBestallning.getId();
    }
}
