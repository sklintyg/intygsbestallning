package se.inera.intyg.intygsbestallning.web.utredning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.lang.invoke.MethodHandles;
import se.inera.intyg.intygsbestallning.common.Bestallning;
import se.inera.intyg.intygsbestallning.common.CreateUtredningRequest;
import se.inera.intyg.intygsbestallning.common.Utredning;
import se.inera.intyg.intygsbestallning.common.Vardenhet;
import se.inera.intyg.intygsbestallning.persistence.service.UtredningPersistenceService;

@Service
public class CreateUtredningServiceImpl implements CreateUtredningService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private final UtredningPersistenceService utredningPersistenceService;

    public CreateUtredningServiceImpl(UtredningPersistenceService utredningPersistenceService) {
        this.utredningPersistenceService = utredningPersistenceService;
    }

    @Override
    public void createUtredning(CreateUtredningRequest createUtredning) {

        LOG.debug("Creating new utredning");

        var bestallning = Bestallning.Factory.newBestallning();
        var utredning = Utredning.Factory.newUtredning(bestallning, new Vardenhet(null, "", null, null));

        utredningPersistenceService.saveNewUtredning(utredning);
    }
}
