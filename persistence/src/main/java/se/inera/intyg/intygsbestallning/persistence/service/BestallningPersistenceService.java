package se.inera.intyg.intygsbestallning.persistence.service;

import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;

public interface BestallningPersistenceService {
    Bestallning saveNewBestallning(Bestallning bestallning);
    ListBestallningarResult listBestallningar(ListBestallningarQuery query);
}
