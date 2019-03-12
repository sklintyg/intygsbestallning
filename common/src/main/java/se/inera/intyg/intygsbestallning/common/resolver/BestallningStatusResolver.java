package se.inera.intyg.intygsbestallning.common.resolver;

import se.inera.intyg.intygsbestallning.common.domain.Bestallning;

public interface BestallningStatusResolver {

    void setStatus(Bestallning bestallning);

}
