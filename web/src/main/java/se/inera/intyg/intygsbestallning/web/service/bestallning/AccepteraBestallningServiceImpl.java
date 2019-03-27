package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.springframework.stereotype.Service;

import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.dto.AccepteraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.integration.client.RespondToOrderService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.service.util.BestallningUtil;

@Service
public class AccepteraBestallningServiceImpl implements AccepteraBestallningService {

    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private RespondToOrderService respondToOrderService;

    public AccepteraBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            BestallningStatusResolver bestallningStatusResolver,
            RespondToOrderService respondToOrderService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.respondToOrderService = respondToOrderService;
    }

    @Override
    public void accepteraBestallning(AccepteraBestallningRequest accepteraBestallningRequest) {

        if (accepteraBestallningRequest == null) {
            throw new IllegalArgumentException("accepteraBestallningRequest may not be null");
        }

        var id = BestallningUtil.resolveId(accepteraBestallningRequest);

        var bestallning = bestallningPersistenceService.getBestallningById(id.get());

        if (bestallning.isEmpty()) {
            throw new IllegalArgumentException("bestallning with id: " + id.get() + " was not found");
        }

        bestallning.get().getHandelser().add(Handelse.Factory.acceptera());
        bestallningStatusResolver.setStatus(bestallning.get());
        bestallningPersistenceService.updateBestallning(bestallning.get());

        respondToOrderService.sendRespondToOrder(accepteraBestallningRequest);
    }
}
