package se.inera.intyg.intygsbestallning.integration.client;

import se.inera.intyg.intygsbestallning.common.dto.AccepteraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.AvvisaBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.SimpleBestallningRequest;

public interface RespondToOrderService {
    void sendRespondToOrder(SimpleBestallningRequest accepteraBestallningRequest);
}
