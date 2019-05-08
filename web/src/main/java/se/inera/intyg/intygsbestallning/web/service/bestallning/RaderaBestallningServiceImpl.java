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

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.dto.RaderaBestallningRequest;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.integration.client.RespondToOrderService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.service.util.BestallningUtil;

@Service
public class RaderaBestallningServiceImpl implements RaderaBestallningService {
    private BestallningPersistenceService bestallningPersistenceService;
    private RespondToOrderService respondToOrderService;
    private BestallningStatusResolver bestallningStatusResolver;

    private static final Logger LOG = LoggerFactory.getLogger(RaderaBestallningServiceImpl.class);

    public RaderaBestallningServiceImpl(BestallningPersistenceService bestallningPersistenceService,
                                        RespondToOrderService respondToOrderService,
                                        BestallningStatusResolver bestallningStatusResolver) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.respondToOrderService = respondToOrderService;
        this.bestallningStatusResolver = bestallningStatusResolver;
    }

    @Override
    @Transactional
    public void raderaBestallning(RaderaBestallningRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("raderaBestallningRequest may not be null");
        }

        Try<Long> id = BestallningUtil.resolveId(request.getBestallningId());

        var bestallning = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(
                id.get(), request.getHsaId(), request.getOrgNrVardgivare());

        if (bestallning.isEmpty()) {
            throw new IllegalArgumentException("bestallning with id: " + id.get() + " was not found");
        }

        bestallning.get().getHandelser().add(Handelse.Factory.avvisaRadera());
        bestallningStatusResolver.setStatus(bestallning.get());

        bestallningPersistenceService.deleteBestallning(bestallning.get());

        LOG.info("Beställning {} raderad", id.get());

        respondToOrderService.sendRespondToOrder(request);
    }
}
