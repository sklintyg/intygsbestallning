package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.springframework.stereotype.Service;
import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningDto;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.integration.pu.PatientService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;

@Service
public class VisaBestallningServiceImpl implements VisaBestallningService {

    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningStatusResolver bestallningStatusResolver;
    private BestallningTextService bestallningTextService;
    private PatientService patientService;

    public VisaBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            BestallningStatusResolver bestallningStatusResolver,
            BestallningTextService bestallningTextService,
            PatientService patientService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningStatusResolver = bestallningStatusResolver;
        this.bestallningTextService = bestallningTextService;
        this.patientService = patientService;
    }

    @Override
    public Optional<VisaBestallningDto> getBestallningById(Long id) {

        var bestallning = bestallningPersistenceService.getBestallningById(id);

        if (bestallning.isEmpty()) {
            return Optional.empty();
        }

        patientService.updatePersonDetaljer(bestallning.get().getInvanare());

        if (bestallning.get().getStatus() == BestallningStatus.OLAST) {
            bestallning.get().getHandelser().add(Handelse.Factory.las());
            bestallningStatusResolver.setStatus(bestallning.get());
            var updatedBestallning = bestallningPersistenceService.updateBestallning(bestallning.get());
            var bestallningTexter = bestallningTextService.getBestallningTexter(updatedBestallning);
            return Optional.of(VisaBestallningDto.Factory.toDto(updatedBestallning, bestallningTexter));
        } else {
            var bestallningTexter = bestallningTextService.getBestallningTexter(bestallning.get());
            return Optional.of(VisaBestallningDto.Factory.toDto(bestallning.get(), bestallningTexter));
        }
    }
}
