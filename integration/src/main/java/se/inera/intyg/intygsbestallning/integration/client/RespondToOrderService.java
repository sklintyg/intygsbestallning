package se.inera.intyg.intygsbestallning.integration.client;

import se.inera.intyg.intygsbestallning.common.AccepteraBestallningRequest;

public interface RespondToOrderService {
    void sendRespondToOrder(AccepteraBestallningRequest accepteraBestallningRequest);
}
