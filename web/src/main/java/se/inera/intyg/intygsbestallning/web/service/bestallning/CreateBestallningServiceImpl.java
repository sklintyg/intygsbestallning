package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.invoke.MethodHandles;
import se.inera.intyg.infra.integration.hsa.services.HsaOrganizationsService;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;
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
            var person = patientService.lookupPersonnummerFromPU(createBestallningRequest.getInvanare().getPersonnummer());

            if (person.isEmpty()) {
                throw new IllegalArgumentException("invanare with personnummer: " +
                        createBestallningRequest.getInvanare().getPersonnummer().getPersonnummerWithDash() + "was not found");
            }

            var foundPerson = person.get();

            invanare = Invanare.Factory.newInvanare(
                    foundPerson.getPersonnummer(), createBestallningRequest.getInvanare().getBakgrundNulage());

        } else {
            invanare = existing.get();
        }

        var vardenhetRespons = hsaOrganizationsService.getVardenhet(createBestallningRequest.getVardenhet());
        var vardGivareRespons = hsaOrganizationsService.getVardgivareOfVardenhet(createBestallningRequest.getVardenhet());

        var existingVardenhet = vardenhetPersistenceService.getVardenhetByHsaId(vardenhetRespons.getId());

        if (vardenhetRespons.getEpost() == null) {
            throw new IllegalArgumentException("Vårdenheten saknar e-postadress");
        }

        Vardenhet vardenhet;
        if (existingVardenhet.isPresent()) {
            vardenhet = existingVardenhet.get();
            vardenhet.setHsaId(vardenhetRespons.getId());
            vardenhet.setVardgivareHsaId(vardGivareRespons.getId());
            vardenhet.setNamn(vardenhetRespons.getNamn());
            vardenhet.setEpost(vardenhetRespons.getEpost());
        } else {
            vardenhet = Vardenhet.Factory.newVardenhet(
                    vardenhetRespons.getId(),
                    vardGivareRespons.getId(),
                    vardGivareRespons.getOrgId(),
                    vardenhetRespons.getNamn(),
                    vardenhetRespons.getEpost(),
                    null);
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
                handlaggare,
                createBestallningRequest.getIntygTyp(),
                createBestallningRequest.getIntygVersion(),
                vardenhet,
                createBestallningRequest.getArendeReferens());

        bestallningStatusResolver.setStatus(bestallning);

        final Bestallning savedBestallning = bestallningPersistenceService.saveNewBestallning(bestallning);
        notifieringSendService.nyBestallning(savedBestallning);

        return savedBestallning.getId();
    }
}
