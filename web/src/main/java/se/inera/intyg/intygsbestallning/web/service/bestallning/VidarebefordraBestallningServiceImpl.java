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

import org.springframework.stereotype.Service;
import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.dto.VidareBefordraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.service.notifiering.NotifieringSendService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.service.util.BestallningUtil;

@Service
public class VidarebefordraBestallningServiceImpl implements VidarebefordraBestallningService {

    private BestallningPersistenceService bestallningPersistenceService;
    private NotifieringSendService notifieringSendService;

    public VidarebefordraBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            NotifieringSendService notifieringSendService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.notifieringSendService = notifieringSendService;
    }

    @Override
    public Optional<String> getVidareBefordraMailBody(VidareBefordraBestallningRequest request) {

        var id = BestallningUtil.resolveId(request.getBestallningId());

        var bestallning = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(
                id.get(), request.getHsaId(), request.getOrgNrVardgivare());

        if (bestallning.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(notifieringSendService.vidarebefordrad(bestallning.get()));
    }
}
