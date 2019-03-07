package se.inera.intyg.intygsbestallning.integration.client;

import se.inera.intyg.intygsbestallning.common.dto.AccepteraBestallningRequest;

public interface RespondToOrderService {
    void sendRespondToOrder(AccepteraBestallningRequest accepteraBestallningRequest);
}
