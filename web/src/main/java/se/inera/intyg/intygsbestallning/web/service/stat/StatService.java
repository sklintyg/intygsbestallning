package se.inera.intyg.intygsbestallning.web.service.stat;

import se.inera.intyg.intygsbestallning.common.dto.StatResponse;

public interface StatService {

    StatResponse getStat(String hsaId, String orgNrVardgivare);

}
