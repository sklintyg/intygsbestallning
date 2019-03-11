package se.inera.intyg.intygsbestallning.web.service.utredning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.lang.invoke.MethodHandles;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.Utredning;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.CreateUtredningRequest;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.persistence.service.UtredningPersistenceService;
import se.inera.intyg.intygsbestallning.web.service.notifiering.NotifieringSendService;

@Service
public class CreateUtredningServiceImpl implements CreateUtredningService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private UtredningPersistenceService utredningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private NotifieringSendService notifieringSendService;

    public CreateUtredningServiceImpl(UtredningPersistenceService utredningPersistenceService,
                                      BestallningStatusResolver bestallningStatusResolver,
                                      NotifieringSendService notifieringSendService) {
        this.utredningPersistenceService = utredningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.notifieringSendService = notifieringSendService;
    }

    @Override
    public Long createUtredning(CreateUtredningRequest createUtredning) {

        LOG.debug("Creating new utredning");

        //TODO: Lookup HSAID

        var hsaId = "hsaId";
        var utredning = Utredning.Factory.newUtredning(hsaId, new Vardenhet(null, "", null, null));
        bestallningStatusResolver.setStatus(utredning);

        utredningPersistenceService.saveNewUtredning(utredning);
        notifieringSendService.notifieraVardenhetsAnvandareNyIntygsbestallning(utredning);

        return utredning.getId();
    }
}
