package se.inera.intyg.intygsbestallning.web.service.bestallning;

import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;

public interface ListBestallningService {
    ListBestallningarResult listByQuery(ListBestallningarQuery query);
}
