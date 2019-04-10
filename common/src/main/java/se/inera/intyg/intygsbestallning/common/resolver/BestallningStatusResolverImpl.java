package se.inera.intyg.intygsbestallning.common.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Handelse;

@Component
public class BestallningStatusResolverImpl implements BestallningStatusResolver {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void setStatus(Bestallning bestallning) {

        var nuvarandeStatus = bestallning.getStatus();

        var senasteHandelse = Collections.max(
                Objects.requireNonNull(bestallning.getHandelser()), Comparator.comparing(Handelse::getSkapad));

        switch (senasteHandelse.getEvent()) {
            case SKAPA:
                if (nuvarandeStatus != BestallningStatus.UNDEFINED) {
                    throw new IllegalStateException("Can not update state from UNDEFINED unless latest handelse is of typ SKAPA");
                }
                bestallning.setStatus(BestallningStatus.OLAST);
                break;
            case LAS:
                if (nuvarandeStatus != BestallningStatus.OLAST) {
                    throw new IllegalStateException("Can not update state from OLAST unless latest handelse is of typ LAST");
                }
                bestallning.setStatus(BestallningStatus.LAST);
                break;
            case AVVISA:
                if (nuvarandeStatus != BestallningStatus.LAST) {
                    throw new IllegalStateException("Can not update state from LAST to AVVISA unless latest handelse is of typ AVVISAD");
                }
                bestallning.setStatus(BestallningStatus.AVVISAD);
                break;
            case ACCEPTERA:
                if (nuvarandeStatus != BestallningStatus.LAST) {
                    throw new IllegalStateException("Can not update state from LAST to ACCEPTERA unless latest handelse is of typ ACCEPTERAD");
                }
                bestallning.setStatus(BestallningStatus.ACCEPTERAD);
                break;
            case KLARMARKERA:
                if (nuvarandeStatus != BestallningStatus.ACCEPTERAD) {
                    throw new IllegalStateException("Can not update state from ACCEPTEDAD to KLAR unless latest handelse is of typ KLARMARKERA");
                }
                bestallning.setStatus(BestallningStatus.KLAR);
                break;
            default:
                throw new IllegalArgumentException("Not a valid bestallningEvent: " + senasteHandelse);
        }
    }
}
