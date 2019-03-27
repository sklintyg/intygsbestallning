package se.inera.intyg.intygsbestallning.web.service.bestallning;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.dto.AvvisaBestallningRequest;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.integration.client.RespondToOrderService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.service.util.BestallningUtil;

@Service
public class AvvisaBestallningServiceImpl implements AvvisaBestallningService {
    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private RespondToOrderService respondToOrderService;

    private static final Logger LOG = LoggerFactory.getLogger(AvvisaBestallningServiceImpl.class);

    public AvvisaBestallningServiceImpl(BestallningPersistenceService bestallningPersistenceService,
                                        BestallningStatusResolver bestallningStatusResolver,
                                        RespondToOrderService respondToOrderService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.respondToOrderService = respondToOrderService;
    }

    @Override
    public void avvisaBestallning(AvvisaBestallningRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("avvisaBestallningRequest may not be null");
        }

        Try<Long> id = BestallningUtil.resolveId(request);

        var bestallning = bestallningPersistenceService.getBestallningById(id.get());

        if (bestallning.isEmpty()) {
            throw new IllegalArgumentException("bestallning with id: " + id.get() + " was not found");
        }
        bestallning.get().getHandelser().add(Handelse.Factory.avvisa());
        bestallningStatusResolver.setStatus(bestallning.get());
        bestallningPersistenceService.updateBestallning(bestallning.get());

        LOG.info("Beställning {} avvisad", id.get());

        respondToOrderService.sendRespondToOrder(request);
    }
}
