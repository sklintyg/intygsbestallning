package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.springframework.stereotype.Service;
import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningDto;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;

@Service
public class VisaBestallningServiceImpl implements VisaBestallningService {

    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningTextService bestallningTextService;

    public VisaBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            BestallningTextService bestallningTextService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningTextService = bestallningTextService;
    }

    @Override
    public Optional<VisaBestallningDto> getBestallningById(Long id) {

        var bestallning = bestallningPersistenceService.getBestallningById(id);

        if (bestallning.isEmpty()) {
            return Optional.empty();
        }

        var bestallningTexter = bestallningTextService.getBestallningTexter(bestallning.get());

        return Optional.of(VisaBestallningDto.Factory.toDto(bestallning.get(), bestallningTexter));
    }
}
