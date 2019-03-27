package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.invoke.MethodHandles;
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

    public CreateBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            InvanarePersistenceService invanarePersistenceService,
            BestallningStatusResolver bestallningStatusResolver,
            VardenhetPersistenceService vardenhetPersistenceService,
            NotifieringSendService notifieringSendService,
            PatientService patientService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.invanarePersistenceService = invanarePersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.vardenhetPersistenceService = vardenhetPersistenceService;
        this.notifieringSendService = notifieringSendService;
        this.patientService = patientService;
    }

    @Override
    public Long create(CreateBestallningRequest createBestallningRequest) {

        LOG.debug("Creating new bestallning");

        //TODO: Lookup HSAID
        var hsaId = "hsaId";

        var existing = invanarePersistenceService.getInvanareByPersonnummer(createBestallningRequest.getInvanare().getPersonnummer());

        Invanare invanare;
        if (existing.isEmpty()) {
            var person = patientService.lookupPersonnummerFromPU(createBestallningRequest.getInvanare().getPersonnummer());

            if (person.isEmpty()) {
                throw new IllegalArgumentException("invanare with personnummer: " +
                        createBestallningRequest.getInvanare().getPersonnummer().getPersonnummerWithDash() + "was not found");
            }

            invanare = Invanare.Factory.newInvanare(
                    createBestallningRequest.getInvanare().getPersonnummer(),
                    createBestallningRequest.getInvanare().getFornamn(),
                    createBestallningRequest.getInvanare().getMellannamn(),
                    createBestallningRequest.getInvanare().getEfternamn(),
                    createBestallningRequest.getInvanare().getBakgrundNulage(),
                    false);
        } else {
            invanare = existing.get();
        }

        var existingVardenhet = vardenhetPersistenceService.getVardenhetByHsaId(createBestallningRequest.getVardenhet());

        Vardenhet vardenhet;
        if (existingVardenhet.isEmpty()) {
            vardenhet = Vardenhet.Factory.newVardenhet(
                    createBestallningRequest.getVardenhet(),
                    "vardgivareHsaId",
                    "namn",
                    "epost",
                    "svar");
        } else {
            vardenhet = existingVardenhet.get();
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
                hsaId,
                invanare,
                handlaggare,
                createBestallningRequest.getIntygTyp(),
                createBestallningRequest.getIntygVersion(),
                vardenhet);

        bestallningStatusResolver.setStatus(bestallning);

        final Bestallning savedBestallning = bestallningPersistenceService.saveNewBestallning(bestallning);
        notifieringSendService.nyBestallning(savedBestallning);

        return savedBestallning.getId();
    }
}
