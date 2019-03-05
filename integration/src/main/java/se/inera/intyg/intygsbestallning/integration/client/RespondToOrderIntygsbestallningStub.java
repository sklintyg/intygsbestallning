package se.inera.intyg.intygsbestallning.integration.client;

import org.springframework.stereotype.Component;
import se.riv.intygsbestallning.certificate.order.respondtoorder.v1.rivtabp21.RespondToOrderResponderInterface;
import se.riv.intygsbestallning.certificate.order.respondtoorderresponder.v1.RespondToOrderResponseType;
import se.riv.intygsbestallning.certificate.order.respondtoorderresponder.v1.RespondToOrderType;

@Component
public class RespondToOrderIntygsbestallningStub implements RespondToOrderResponderInterface {

    @Override
    public RespondToOrderResponseType respondToOrder(String s, RespondToOrderType respondToOrderType) {
        return null;
    }
}
