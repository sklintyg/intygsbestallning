package se.inera.intyg.intygsbestallning.persistence;

import se.inera.intyg.intygsbestallning.common.Utredning;

public interface BestallningPersistenceService {

    Utredning saveNewUtredning(Utredning utredning);

}
