package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.lang.invoke.MethodHandles;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.common.service.notifiering.NotifieringSendService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;

@Service
public class CreateBestallningServiceImpl implements CreateBestallningService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private NotifieringSendService notifieringSendService;

    public CreateBestallningServiceImpl(BestallningPersistenceService bestallningPersistenceService,
                                        BestallningStatusResolver bestallningStatusResolver,
                                        NotifieringSendService notifieringSendService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.notifieringSendService = notifieringSendService;
    }

    @Override
    public Long create(CreateBestallningRequest createBestallningRequest) {

        LOG.debug("Creating new bestallning");

        //TODO: Lookup HSAID
        var hsaId = "hsaId";

        //TODO: Lookup Invanare
        var invanare = new Invanare(createBestallningRequest.getPersonnummer(), null, null, null, null, null);

        //TODO: Lookup Vardenhet
        var vardenhet = new Vardenhet(null, "", null, null);

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
