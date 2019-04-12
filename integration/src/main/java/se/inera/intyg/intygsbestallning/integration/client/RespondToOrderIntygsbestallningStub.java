package se.inera.intyg.intygsbestallning.integration.client;

import org.springframework.stereotype.Component;
import se.riv.intygsbestallning.certificate.order.respondtoorder.v1.rivtabp21.RespondToOrderResponderInterface;
import se.riv.intygsbestallning.certificate.order.respondtoorderresponder.v1.RespondToOrderResponseType;
import se.riv.intygsbestallning.certificate.order.respondtoorderresponder.v1.RespondToOrderType;
import se.riv.intygsbestallning.certificate.order.v1.ResultCodeType;
import se.riv.intygsbestallning.certificate.order.v1.ResultType;

@Component
public class RespondToOrderIntygsbestallningStub implements RespondToOrderResponderInterface {

    @Override
    public RespondToOrderResponseType respondToOrder(String s, RespondToOrderType respondToOrderType) {
        var response = new RespondToOrderResponseType();
        var result = new ResultType();
        result.setResultCode(ResultCodeType.OK);
        response.setResult(result);
        return response;
    }
}
