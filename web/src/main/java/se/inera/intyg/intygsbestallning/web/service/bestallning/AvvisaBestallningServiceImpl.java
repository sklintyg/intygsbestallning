package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.vavr.control.Try;
import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.dto.AvvisaBestallningRequest;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.integration.client.RespondToOrderService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.service.pdl.LogService;
import se.inera.intyg.intygsbestallning.web.service.util.BestallningUtil;

@Service
public class AvvisaBestallningServiceImpl implements AvvisaBestallningService {
    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private RespondToOrderService respondToOrderService;
    private LogService pdlLogService;

    private static final Logger LOG = LoggerFactory.getLogger(AvvisaBestallningServiceImpl.class);

    public AvvisaBestallningServiceImpl(BestallningPersistenceService bestallningPersistenceService,
                                        BestallningStatusResolver bestallningStatusResolver,
                                        RespondToOrderService respondToOrderService,
                                        LogService pdlLogService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.respondToOrderService = respondToOrderService;
        this.pdlLogService = pdlLogService;
    }

    @Override
    public void avvisaBestallning(AvvisaBestallningRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("avvisaBestallningRequest may not be null");
        }

        Try<Long> id = BestallningUtil.resolveId(request);

        var bestallning = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(
                id.get(), request.getHsaId(), request.getOrgNrVardgivare());

        if (bestallning.isEmpty()) {
            throw new IllegalArgumentException("bestallning with id: " + id.get() + " was not found");
        }
        bestallning.get().getHandelser().add(Handelse.Factory.avvisa());
        bestallningStatusResolver.setStatus(bestallning.get());
        bestallningPersistenceService.updateBestallning(bestallning.get());

        LOG.info("Beställning {} avvisad", id.get());
        pdlLogService.log(bestallning.get(), LogEvent.BESTALLNING_AVVISAS);

        respondToOrderService.sendRespondToOrder(request);
    }
}
