package se.inera.intyg.intygsbestallning.common.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Handelse;
import se.inera.intyg.intygsbestallning.common.domain.Utredning;

@Component
public class BestallningStatusResolverImpl implements BestallningStatusResolver {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void setStatus(Utredning utredning) {

        var nuvarandeStatus = utredning.getBestallning().getStatus();

        var senasteHandelse = Collections.max(
                Objects.requireNonNull(utredning.getBestallning().getHandelser()), Comparator.comparing(Handelse::getSkapad));

        switch (senasteHandelse.getEvent()) {
            case SKAPA:
                if (nuvarandeStatus != BestallningStatus.UNDEFINED) {
                    throw new IllegalStateException("Can not update state from UNDEFINED unless latest handelse is of typ SKAPA");
                }
                utredning.getBestallning().setStatus(BestallningStatus.OLAST);
                break;
            case LAS:
                if (nuvarandeStatus != BestallningStatus.OLAST) {
                    throw new IllegalStateException("Can not update state from OLAST unless latest handelse is of typ LAST");
                }
                utredning.getBestallning().setStatus(BestallningStatus.LAST);
                break;
            case AVVISA:
                if (nuvarandeStatus != BestallningStatus.LAST) {
                    throw new IllegalStateException("Can not update state from LAST to AVVISA unless latest handelse is of typ AVVISAD");
                }
                utredning.getBestallning().setStatus(BestallningStatus.AVVISAD);
                break;
            case AVVISA_RADERA:
                if (nuvarandeStatus != BestallningStatus.LAST) {
                    throw new IllegalStateException("Can not update state from LAST to AVVISAD_RADERAD unless latest handelse is of typ AVVISA_RADERA");
                }
                utredning.getBestallning().setStatus(BestallningStatus.AVVISAD_RADERAD);
                break;
            case ACCEPTERA:
                if (nuvarandeStatus != BestallningStatus.LAST) {
                    throw new IllegalStateException("Can not update state from LAST to ACCEPTERA unless latest handelse is of typ ACCEPTERAD");
                }
                utredning.getBestallning().setStatus(BestallningStatus.ACCEPTERAD);
                break;
            case KLARMARKERA:
                if (nuvarandeStatus != BestallningStatus.LAST) {
                    throw new IllegalStateException("Can not update state from ACCEPTEDAD to KLARMARKERAD unless latest handelse is of typ KLARMARKERA");
                }
                utredning.getBestallning().setStatus(BestallningStatus.KLARMARKERAD);
                break;
            default:
                throw new IllegalArgumentException("Not a valid bestallningEvent: " + senasteHandelse);
        }
    }
}
