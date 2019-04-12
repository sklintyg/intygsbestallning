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
