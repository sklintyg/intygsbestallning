package se.inera.intyg.intygsbestallning.common.utredning;

import se.inera.intyg.intygsbestallning.common.BestallningEvent;
import se.inera.intyg.intygsbestallning.common.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.Utredning;

public interface StatusResolver {

    BestallningStatus getNextStatus(Utredning utredning, BestallningEvent event);

}
