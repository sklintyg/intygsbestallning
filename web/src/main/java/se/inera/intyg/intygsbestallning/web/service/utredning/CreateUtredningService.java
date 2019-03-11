package se.inera.intyg.intygsbestallning.web.service.utredning;


import se.inera.intyg.intygsbestallning.common.dto.CreateUtredningRequest;

public interface CreateUtredningService {

    Long createUtredning(CreateUtredningRequest createUtredning);
}
