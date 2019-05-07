/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarResult;
import se.inera.intyg.intygsbestallning.common.dto.PageDto;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.service.pdl.LogService;

import java.util.List;

@Service
public class ListBestallningServiceImpl implements ListBestallningService {

    private BestallningPersistenceService bestallningPersistenceService;

    private LogService pdlLogService;

    public ListBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            LogService pdlLogService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.pdlLogService = pdlLogService;
    }

    @Override
    @Transactional(readOnly = true)
    public ListBestallningarResult listByQuery(ListBestallningarQuery query) {
        Pair<PageDto, List<Bestallning>> result = bestallningPersistenceService.listBestallningar(query);

        PageDto pageDto = result.getFirst();
        List<Bestallning> bestallningar = result.getSecond();

        // Do the mandatory PDL-logging
        pdlLogService.logList(bestallningar, LogEvent.PERSONINFORMATION_VISAS_I_LISTA);

        return ListBestallningarResult.Factory.toDto(
                query.getTextSearch() != null,
                bestallningar,
                pageDto,
                query.getSortColumn(),
                query.getSortDirection());
    }

}
