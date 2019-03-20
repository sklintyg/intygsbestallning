package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.springframework.stereotype.Service;
import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.dto.AktuellBestallningDto;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;

@Service
public class GetBestallningServiceImpl implements GetBestallningService {

    private BestallningPersistenceService bestallningPersistenceService;

    public GetBestallningServiceImpl(BestallningPersistenceService bestallningPersistenceService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
    }

    @Override
    public Optional<AktuellBestallningDto> getBestallningById(Long id) {

        //TODO: READ text from bestallning-text-file

        return bestallningPersistenceService.getBestallningById(id)
                .map(AktuellBestallningDto.Factory::toDto);
    }
}
