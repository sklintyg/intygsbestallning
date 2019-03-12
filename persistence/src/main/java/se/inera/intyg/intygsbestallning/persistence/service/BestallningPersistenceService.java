package se.inera.intyg.intygsbestallning.persistence.service;

import se.inera.intyg.intygsbestallning.common.domain.Bestallning;

public interface BestallningPersistenceService {
    Bestallning saveNewBestallning(Bestallning bestallning);
}
