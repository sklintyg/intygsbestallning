package se.inera.intyg.intygsbestallning.persistence;

import se.inera.intyg.intygsbestallning.common.Utredning;

public interface UtredningPersistenceService {

    Utredning saveNewUtredning(Utredning utredning);

}
