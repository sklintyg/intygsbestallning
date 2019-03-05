package se.inera.intyg.intygsbestallning.common.utredning;

import org.springframework.stereotype.Component;
import se.inera.intyg.intygsbestallning.common.BestallningEvent;
import se.inera.intyg.intygsbestallning.common.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.Utredning;

@Component
public class BestallningStatusResolver implements StatusResolver {

    @Override
    public BestallningStatus getNextStatus(Utredning utredning, BestallningEvent event) {

        return null;
    }
}
