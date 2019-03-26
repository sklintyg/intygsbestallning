package se.inera.intyg.intygsbestallning.web.service.bestallning;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;

@Service
public class ListBestallningServiceImpl implements ListBestallningService {

    private BestallningPersistenceService bestallningPersistenceService;

    public ListBestallningServiceImpl(BestallningPersistenceService bestallningPersistenceService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
    }

    @Override
    @Transactional(readOnly = true)
    public ListBestallningarResult listByQuery(ListBestallningarQuery query) {
        return bestallningPersistenceService.listBestallningar(query);
    }

    @Override
    public Page<Bestallning> list(Predicate predicate, Pageable pageable) {
        return bestallningPersistenceService.list(predicate, pageable);
    }
}
