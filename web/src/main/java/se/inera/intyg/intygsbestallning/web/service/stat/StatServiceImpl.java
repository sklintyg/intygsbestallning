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

package se.inera.intyg.intygsbestallning.web.service.stat;

import java.util.List;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.dto.CountBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.StatResponse;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;

@Service
public class StatServiceImpl implements StatService {

    private BestallningPersistenceService bestallningPersistenceService;

    public StatServiceImpl(BestallningPersistenceService bestallningPersistenceService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
    }

    @Override
    public StatResponse getStat(String hsaId, String orgNrVardgivare) {

        long antalOlastaBestallningar = bestallningPersistenceService.countBestallningar(
                new CountBestallningarQuery(List.of(BestallningStatus.OLAST), hsaId, orgNrVardgivare));

        long antalAktivaBestallningar = bestallningPersistenceService.countBestallningar(
                new CountBestallningarQuery(List.of(BestallningStatus.ACCEPTERAD, BestallningStatus.LAST), hsaId, orgNrVardgivare));

        long antalKlaraBestallningar = bestallningPersistenceService.countBestallningar(
                new CountBestallningarQuery(List.of(BestallningStatus.KLAR), hsaId, orgNrVardgivare));

        return new StatResponse(antalOlastaBestallningar, antalAktivaBestallningar, antalKlaraBestallningar);
    }
}
