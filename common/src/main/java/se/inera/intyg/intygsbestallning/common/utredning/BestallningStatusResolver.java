package se.inera.intyg.intygsbestallning.common.utredning;

import se.inera.intyg.intygsbestallning.common.domain.Utredning;

public interface BestallningStatusResolver {

    void setStatus(Utredning utredning);

}
