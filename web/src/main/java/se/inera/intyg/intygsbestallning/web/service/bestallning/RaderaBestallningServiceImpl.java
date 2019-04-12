package se.inera.intyg.intygsbestallning.web.service.bestallning;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.inera.intyg.intygsbestallning.common.dto.RaderaBestallningRequest;
import se.inera.intyg.intygsbestallning.integration.client.RespondToOrderService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.service.util.BestallningUtil;

@Service
public class RaderaBestallningServiceImpl implements RaderaBestallningService {
    private BestallningPersistenceService bestallningPersistenceService;
    private RespondToOrderService respondToOrderService;

    private static final Logger LOG = LoggerFactory.getLogger(RaderaBestallningServiceImpl.class);

    public RaderaBestallningServiceImpl(BestallningPersistenceService bestallningPersistenceService,
                                        RespondToOrderService respondToOrderService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.respondToOrderService = respondToOrderService;
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

        bestallningPersistenceService.deleteBestallning(bestallning.get());

        LOG.info("Beställning {} raderad", id.get());

        respondToOrderService.sendRespondToOrder(request);
    }
}
