package se.inera.intyg.intygsbestallning.web.service.stat;

import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.dto.CountBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.StatResponse;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;

import java.util.List;

@Service
public class StatServiceImpl implements StatService {

    private BestallningPersistenceService bestallningPersistenceService;

    public StatServiceImpl(BestallningPersistenceService bestallningPersistenceService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
    }

    @Override
    public StatResponse getStat(String hsaId, String orgNrVardgivare) {

        long antalOlastaBestallningar = bestallningPersistenceService.countBestallningar(
                new CountBestallningarQuery(List.of(BestallningStatus.OLAST), hsaId, orgNrVardgivare));

        long antalAktivaBestallningar = bestallningPersistenceService.countBestallningar(
                new CountBestallningarQuery(List.of(BestallningStatus.ACCEPTERAD, BestallningStatus.OLAST, BestallningStatus.LAST), hsaId, orgNrVardgivare));

        long antalKlaraBestallningar = bestallningPersistenceService.countBestallningar(
                new CountBestallningarQuery(List.of(BestallningStatus.KLAR), hsaId, orgNrVardgivare));

        return new StatResponse(antalOlastaBestallningar, antalAktivaBestallningar, antalKlaraBestallningar);
    }
}
