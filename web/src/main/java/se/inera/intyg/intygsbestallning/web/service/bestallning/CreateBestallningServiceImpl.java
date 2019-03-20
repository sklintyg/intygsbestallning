package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.invoke.MethodHandles;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.common.service.notifiering.NotifieringSendService;
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

    public CreateBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            InvanarePersistenceService invanarePersistenceService,
            BestallningStatusResolver bestallningStatusResolver,
            VardenhetPersistenceService vardenhetPersistenceService,
            NotifieringSendService notifieringSendService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.invanarePersistenceService = invanarePersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.vardenhetPersistenceService = vardenhetPersistenceService;
        this.notifieringSendService = notifieringSendService;
    }

    @Override
    public Long create(CreateBestallningRequest createBestallningRequest) {

        LOG.debug("Creating new bestallning");

        //TODO: Lookup HSAID
        var hsaId = "hsaId";

        var existingInvanare = invanarePersistenceService.getInvanareByPersonnummer(createBestallningRequest.getPersonnummer());

        Invanare invanare;
        if (existingInvanare.isEmpty()) {
            invanare = Invanare.Factory.newInvanare(
                    createBestallningRequest.getPersonnummer(),
                    createBestallningRequest.getFornamn(),
                    createBestallningRequest.getMellannamn(),
                    createBestallningRequest.getEfternamn(),
                    createBestallningRequest.getBakgrundNulage(),
                    createBestallningRequest.getSektretessMarkering());
        } else {
            invanare = existingInvanare.get();
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

        var bestallning = Bestallning.Factory.newBestallning(
                hsaId,
                invanare,
                createBestallningRequest.getIntygTyp(),
                vardenhet);

        bestallningStatusResolver.setStatus(bestallning);

        final Bestallning savedBestallning = bestallningPersistenceService.saveNewBestallning(bestallning);
        notifieringSendService.nyBestallning(savedBestallning);

        return savedBestallning.getId();
    }
}
