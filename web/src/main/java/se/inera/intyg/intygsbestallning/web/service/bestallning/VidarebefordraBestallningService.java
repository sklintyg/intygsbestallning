package se.inera.intyg.intygsbestallning.web.service.bestallning;

import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.dto.VidareBefordraBestallningRequest;

public interface VidarebefordraBestallningService {
    Optional<String> getVidareBefordraMailBody(VidareBefordraBestallningRequest request);
}
