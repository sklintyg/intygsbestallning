package se.inera.intyg.intygsbestallning.persistence.service;

import se.inera.intyg.intygsbestallning.common.domain.Utredning;

public interface UtredningPersistenceService {
    Utredning saveNewUtredning(Utredning utredning);
}
