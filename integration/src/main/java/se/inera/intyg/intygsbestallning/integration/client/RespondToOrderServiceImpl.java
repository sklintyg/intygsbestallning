package se.inera.intyg.intygsbestallning.integration.client;

import org.springframework.stereotype.Service;
import se.riv.intygsbestallning.certificate.order.respondtoorder.v1.rivtabp21.RespondToOrderResponderInterface;
import se.inera.intyg.intygsbestallning.common.AccepteraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.IntegrationDtosKt;

@Service
public class RespondToOrderServiceImpl implements RespondToOrderService {

    private final RespondToOrderResponderInterface respondToOrderResponderInterface;

    public RespondToOrderServiceImpl(RespondToOrderResponderInterface respondToOrderResponderInterface) {
        this.respondToOrderResponderInterface = respondToOrderResponderInterface;
    }

    @Override
    public void sendRespondToOrder(AccepteraBestallningRequest accepteraBestallningRequest) {
        var respondToOrderType = IntegrationDtosKt.toRespondToOrderType(accepteraBestallningRequest);
        respondToOrderResponderInterface.respondToOrder("", respondToOrderType);
    }
}
