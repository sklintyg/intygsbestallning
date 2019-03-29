package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.springframework.stereotype.Service;

import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.dto.AccepteraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.integration.client.RespondToOrderService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.service.pdl.LogService;
import se.inera.intyg.intygsbestallning.web.service.util.BestallningUtil;

@Service
public class AccepteraBestallningServiceImpl implements AccepteraBestallningService {

    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private RespondToOrderService respondToOrderService;
    private LogService pdlLogService;

    public AccepteraBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            BestallningStatusResolver bestallningStatusResolver,
            RespondToOrderService respondToOrderService,
            LogService pdlLogService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.respondToOrderService = respondToOrderService;
        this.pdlLogService = pdlLogService;
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

        pdlLogService.log(bestallning.get(), LogEvent.BESTALLNING_ACCEPTERAS);
        respondToOrderService.sendRespondToOrder(accepteraBestallningRequest);
    }
}
