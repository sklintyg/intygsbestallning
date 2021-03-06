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

package se.inera.intyg.intygsbestallning.persistence.service;

import org.springframework.data.util.Pair;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.dto.CountBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarBasedOnStatusQuery;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.PageDto;

import java.util.List;
import java.util.Optional;

public interface BestallningPersistenceService {
    Bestallning saveNewBestallning(Bestallning bestallning);

    Bestallning updateBestallning(Bestallning bestallning);

    void deleteBestallning(Bestallning bestallning);

    long countBestallningar(CountBestallningarQuery query);

    Pair<PageDto, List<Bestallning>> listBestallningar(ListBestallningarQuery query);

    Optional<Bestallning> getBestallningByIdAndHsaIdAndOrgId(Long id, String hsaId, String orgNrVardgivare);

    Optional<Bestallning> getBestallningById(Long id);

    List<Bestallning> listBestallningarBasedOnStatus(ListBestallningarBasedOnStatusQuery query);
}
