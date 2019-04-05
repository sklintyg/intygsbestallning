package se.inera.intyg.intygsbestallning.persistence.service;

import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.dto.CountBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;

public interface BestallningPersistenceService {
    Bestallning saveNewBestallning(Bestallning bestallning);

    Bestallning updateBestallning(Bestallning bestallning);

    void deleteBestallning(Bestallning bestallning);

    long countBestallningar(CountBestallningarQuery query);

    ListBestallningarResult listBestallningar(ListBestallningarQuery query);

    Optional<Bestallning> getBestallningByIdAndHsaIdAndOrgId(Long id, String hsaId, String orgNrVardgivare);
}
