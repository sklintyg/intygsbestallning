package se.inera.intyg.intygsbestallning.common.utredning;

import se.inera.intyg.intygsbestallning.common.BestallningEvent;
import se.inera.intyg.intygsbestallning.common.BestallningStatus;

public interface BestallningStatusResolver {

    BestallningStatus getNextStatus(BestallningEvent event);

}
