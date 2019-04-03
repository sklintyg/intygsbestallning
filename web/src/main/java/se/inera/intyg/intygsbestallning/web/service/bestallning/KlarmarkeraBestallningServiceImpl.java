package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.dto.KlarmarkeraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.integration.client.RespondToOrderService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.service.pdl.LogService;
import se.inera.intyg.intygsbestallning.web.service.util.BestallningUtil;

@Service
public class KlarmarkeraBestallningServiceImpl implements KlarmarkeraBestallningService {

    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private LogService pdlLogService;

    public KlarmarkeraBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            BestallningStatusResolver bestallningStatusResolver,
            RespondToOrderService respondToOrderService,
            LogService pdlLogService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.pdlLogService = pdlLogService;
    }

    @Override
    public void klarmarkeraBestallning(KlarmarkeraBestallningRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("request may not be null");
        }

        var id = BestallningUtil.resolveId(request.getBestallningId());

        var bestallning = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(
                id.get(), request.getHsaId(), request.getOrgNrVardgivare());

        if (bestallning.isEmpty()) {
            throw new IllegalArgumentException("bestallning with id: " + id.get() + " was not found");
        }

        bestallning.get().getHandelser().add(Handelse.Factory.klarmarkera());
        bestallningStatusResolver.setStatus(bestallning.get());
        bestallningPersistenceService.updateBestallning(bestallning.get());

        pdlLogService.log(bestallning.get(), LogEvent.BESTALLNING_KLARMARKERAS);
    }
}
