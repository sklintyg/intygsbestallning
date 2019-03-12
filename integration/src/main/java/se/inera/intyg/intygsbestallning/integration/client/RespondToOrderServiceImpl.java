package se.inera.intyg.intygsbestallning.integration.client;

import org.springframework.stereotype.Service;
import se.riv.intygsbestallning.certificate.order.respondtoorder.v1.rivtabp21.RespondToOrderResponderInterface;
import se.riv.intygsbestallning.certificate.order.respondtoorderresponder.v1.RespondToOrderType;
import se.inera.intyg.intygsbestallning.common.dto.AccepteraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.util.RivtaUtil;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;

@Service
public class RespondToOrderServiceImpl implements RespondToOrderService {

    private final RespondToOrderResponderInterface respondToOrderResponderInterface;
    private final IntegrationProperties integrationProperties;

    public RespondToOrderServiceImpl(
            RespondToOrderResponderInterface respondToOrderResponderInterface,
            IntegrationProperties integrationProperties) {
        this.respondToOrderResponderInterface = respondToOrderResponderInterface;
        this.integrationProperties = integrationProperties;
    }

    @Override
    public void sendRespondToOrder(AccepteraBestallningRequest accepteraBestallningRequest) {
        var respondToOrderType = createResponderType(accepteraBestallningRequest);
        respondToOrderResponderInterface.respondToOrder(integrationProperties.getSourceSystemHsaId(), respondToOrderType);
    }

    private RespondToOrderType createResponderType(AccepteraBestallningRequest request) {

        var assessmentId = RivtaUtil.anII(integrationProperties.getSourceSystemHsaId(), request.getBestallningId());

        var respondToOrderType = new RespondToOrderType();
        respondToOrderType.setAssessmentId(assessmentId);
        respondToOrderType.setComment(request.getFritextForklaring());

        return respondToOrderType;
    }
}
