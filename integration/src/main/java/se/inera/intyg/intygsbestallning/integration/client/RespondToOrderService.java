package se.inera.intyg.intygsbestallning.integration.client;

import se.inera.intyg.intygsbestallning.common.dto.AccepteraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.AvvisaBestallningRequest;

public interface RespondToOrderService {
    void sendRespondToOrder(AccepteraBestallningRequest accepteraBestallningRequest);

    void sendRespondToOrder(AvvisaBestallningRequest avvisaBestallningRequest);
}
