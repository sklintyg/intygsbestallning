package se.inera.intyg.intygsbestallning.web.utredning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.CreateUtredningRequest;
import se.inera.intyg.intygsbestallning.common.DomainObjectsKt;
import se.inera.intyg.intygsbestallning.common.Vardenhet;
import se.inera.intyg.intygsbestallning.persistence.BestallningPersistenceService;

@Service
public class CreateUtredningServiceImpl implements CreateUtredningService {


    @Autowired
    private BestallningPersistenceService bestallningPersistenceService;

    @Override
    public void createUtredning(CreateUtredningRequest createUtredning) {

        var bestallning = DomainObjectsKt.newBestallning();
        var utredning = DomainObjectsKt.newUtredning(bestallning, new Vardenhet(null, "", null, null));

        bestallningPersistenceService.saveNewUtredning(utredning);
    }
}
