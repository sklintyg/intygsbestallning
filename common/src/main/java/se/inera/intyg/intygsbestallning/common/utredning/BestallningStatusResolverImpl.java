package se.inera.intyg.intygsbestallning.common.utredning;

import org.springframework.stereotype.Component;
import se.inera.intyg.intygsbestallning.common.BestallningEvent;
import se.inera.intyg.intygsbestallning.common.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.Utredning;

@Component
public class BestallningStatusResolverImpl implements BestallningStatusResolver {

    @Override
    public BestallningStatus getNextStatus(BestallningEvent event) {

        switch (event) {
            case SKAPA:
                return BestallningStatus.OLAST;
            case LAS:
                return BestallningStatus.LAST;
            case AVVISA:
                return BestallningStatus.AVVISAD;
            case AVVISA_RADERA:
                return BestallningStatus.AVVISAD_RADERAD;
            case ACCEPTERA:
                return BestallningStatus.ACCEPTERAD;
            case KLARMARKERA:
                return BestallningStatus.KLARMARKERAD;
            default:
                throw new IllegalArgumentException("Not a valid bestallningEvent: " + event);
        }
    }
}
