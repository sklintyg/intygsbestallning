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

@Service
@Transactional
public class CreateBestallningServiceImpl implements CreateBestallningService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private NotifieringSendService notifieringSendService;
    private PatientService patientService;
    private HsaOrganizationsService hsaOrganizationsService;

    public CreateBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            BestallningStatusResolver bestallningStatusResolver,
            NotifieringSendService notifieringSendService,
            PatientService patientService,
            HsaOrganizationsService hsaOrganizationsService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.notifieringSendService = notifieringSendService;
        this.patientService = patientService;
        this.hsaOrganizationsService = hsaOrganizationsService;
    }

    @Override
    public Long create(CreateBestallningRequest createBestallningRequest) {

        LOG.debug("Creating new bestallning");

        var person = patientService.lookupPersonnummerFromPU(createBestallningRequest.getInvanare().getPersonnummer());

        if (person.isEmpty()) {
            throw new IllegalArgumentException("invanare with personnummer: " +
                    createBestallningRequest.getInvanare().getPersonnummer().getPersonnummerWithDash() + " was not found");
        }

        var foundPerson = person.get();

        var invanare = Invanare.Factory.newInvanare(
                foundPerson.getPersonnummer(), createBestallningRequest.getInvanare().getBakgrundNulage());

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

        Vardenhet vardenhet = Vardenhet.Factory.newVardenhet(
                    vardenhetRespons.get().getId(),
                    vardGivareRespons.getId(),
                    vardGivareRespons.getOrgId(),
                    vardenhetRespons.get().getNamn(),
                    vardenhetRespons.get().getEpost());

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
