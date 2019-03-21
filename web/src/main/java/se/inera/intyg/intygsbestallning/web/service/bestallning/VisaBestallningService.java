package se.inera.intyg.intygsbestallning.web.service.bestallning;

import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningDto;

public interface VisaBestallningService {
    Optional<VisaBestallningDto> getBestallningById(Long id);
}
