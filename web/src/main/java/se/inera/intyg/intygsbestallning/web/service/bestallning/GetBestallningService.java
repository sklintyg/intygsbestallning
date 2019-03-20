package se.inera.intyg.intygsbestallning.web.service.bestallning;

import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.dto.AktuellBestallningDto;

public interface GetBestallningService {
    Optional<AktuellBestallningDto> getBestallningById(Long id);
}
