package se.inera.intyg.intygsbestallning.persistence.service;

import com.querydsl.core.types.Predicate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;

public interface BestallningPersistenceService {
    Bestallning saveNewBestallning(Bestallning bestallning);

    Bestallning updateBestallning(Bestallning bestallning);

    ListBestallningarResult listBestallningar(ListBestallningarQuery query);

    Optional<Bestallning> getBestallningByIdAndHsaIdAndOrgId(Long id, String hsaId, String orgNrVardgivare);

    Page<Bestallning> list(Predicate predicate, Pageable pageable);
}
