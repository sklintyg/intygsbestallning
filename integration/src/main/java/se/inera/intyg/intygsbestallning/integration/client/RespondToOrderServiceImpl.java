package se.inera.intyg.intygsbestallning.integration.client;

import org.springframework.stereotype.Service;
import se.riv.intygsbestallning.certificate.order.respondtoorder.v1.rivtabp21.RespondToOrderResponderInterface;
import se.riv.intygsbestallning.certificate.order.respondtoorderresponder.v1.RespondToOrderType;
import se.riv.intygsbestallning.certificate.order.v1.IIType;
import se.inera.intyg.intygsbestallning.common.dto.AccepteraBestallningRequest;

@Service
public class RespondToOrderServiceImpl implements RespondToOrderService {

    private final RespondToOrderResponderInterface respondToOrderResponderInterface;

    public RespondToOrderServiceImpl(RespondToOrderResponderInterface respondToOrderResponderInterface) {
        this.respondToOrderResponderInterface = respondToOrderResponderInterface;
    }

    @Override
    public void sendRespondToOrder(AccepteraBestallningRequest accepteraBestallningRequest) {
        var respondToOrderType = createResponderType(accepteraBestallningRequest);
        respondToOrderResponderInterface.respondToOrder("", respondToOrderType);
    }

    private RespondToOrderType createResponderType(AccepteraBestallningRequest request) {

        var assesmentId = new IIType();
        assesmentId.setExtension(request.getUtredningId());

        var respondToOrderType = new RespondToOrderType();
        respondToOrderType.setAssessmentId(assesmentId);
        respondToOrderType.setComment(request.getFritextForklaring());

        return respondToOrderType;
    }
}
