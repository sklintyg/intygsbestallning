package se.inera.intyg.intygsbestallning.common.service.bestallning;

import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter;

public interface BestallningTextService {
    BestallningTexter getBestallningTexter(Bestallning bestallning);

    Double getLatestVersionForBestallningsbartIntyg(String intygTyp);
}
