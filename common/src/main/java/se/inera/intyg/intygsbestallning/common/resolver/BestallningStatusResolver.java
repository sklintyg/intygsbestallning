package se.inera.intyg.intygsbestallning.common.resolver;

import se.inera.intyg.intygsbestallning.common.domain.Utredning;

public interface BestallningStatusResolver {

    void setStatus(Utredning utredning);

}
