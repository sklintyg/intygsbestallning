package se.inera.intyg.intygsbestallning.web.bestallning;

import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.AccepteraBestallningRequest;
import se.inera.intyg.intygsbestallning.integration.client.RespondToOrderService;

@Service
public class AccepteraBestallningServiceImpl implements AccepteraBestallningService {

    private final RespondToOrderService respondToOrderService;

    public AccepteraBestallningServiceImpl(RespondToOrderService respondToOrderService) {
        this.respondToOrderService = respondToOrderService;
    }

    @Override
    public void accepteraBestallning(AccepteraBestallningRequest accepteraBestallningRequest) {

        if (accepteraBestallningRequest == null) {
            throw new IllegalArgumentException("accepteraBestallningRequest may not be null");
        }

        respondToOrderService.sendRespondToOrder(accepteraBestallningRequest);
    }
}
