package se.inera.intyg.intygsbestallning.web.service.utredning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.lang.invoke.MethodHandles;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.Utredning;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.CreateUtredningRequest;
import se.inera.intyg.intygsbestallning.common.utredning.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.persistence.service.UtredningPersistenceService;

@Service
public class CreateUtredningServiceImpl implements CreateUtredningService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private UtredningPersistenceService utredningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;

    public CreateUtredningServiceImpl(UtredningPersistenceService utredningPersistenceService,
                                      BestallningStatusResolver bestallningStatusResolver) {
        this.utredningPersistenceService = utredningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
    }

    @Override
    public void createUtredning(CreateUtredningRequest createUtredning) {

        LOG.debug("Creating new utredning");

        var bestallning = Bestallning.Factory.newBestallning();
        var utredning = Utredning.Factory.newUtredning(bestallning, new Vardenhet(null, "", null, null));

        bestallningStatusResolver.setStatus(utredning);

        utredningPersistenceService.saveNewUtredning(utredning);
    }
}
