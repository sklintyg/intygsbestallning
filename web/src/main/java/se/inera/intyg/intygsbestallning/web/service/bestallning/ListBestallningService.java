package se.inera.intyg.intygsbestallning.web.service.bestallning;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;

public interface ListBestallningService {
    ListBestallningarResult listByQuery(ListBestallningarQuery query);

    Page<Bestallning> list(Predicate predicate, Pageable pageable);
}
