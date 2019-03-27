package se.inera.intyg.intygsbestallning.integration.client;

import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.BestallningSvar;
import se.inera.intyg.intygsbestallning.common.dto.AvvisaBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.SimpleBestallningRequest;
import se.riv.intygsbestallning.certificate.order.respondtoorder.v1.rivtabp21.RespondToOrderResponderInterface;
import se.riv.intygsbestallning.certificate.order.respondtoorderresponder.v1.RespondToOrderType;
import se.inera.intyg.intygsbestallning.common.dto.AccepteraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.util.RivtaUtil;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.riv.intygsbestallning.certificate.order.v1.CVType;

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

    @Override
    public void sendRespondToOrder(AvvisaBestallningRequest avvisaBestallningRequest) {
        var respondToOrderType = createResponderType(avvisaBestallningRequest);
        respondToOrderResponderInterface.respondToOrder(integrationProperties.getSourceSystemHsaId(), respondToOrderType);
    }

    private RespondToOrderType createResponderType(SimpleBestallningRequest request) {

        var assessmentId = RivtaUtil.anII(integrationProperties.getSourceSystemHsaId(), request.getBestallningId());

        var respondToOrderType = new RespondToOrderType();
        respondToOrderType.setAssessmentId(assessmentId);
        respondToOrderType.setResponse(buildCVType(request.getBestallningSvar()));
        respondToOrderType.setComment(request.getFritextForklaring());

        return respondToOrderType;
    }

    private CVType buildCVType(BestallningSvar bestallningSvar) {
        CVType cvType = new CVType();
        cvType.setCode(bestallningSvar.getCode());
        cvType.setCodeSystem(bestallningSvar.getCodeSystem());
        cvType.setCodeSystemName(bestallningSvar.getCodeSystemName());
        cvType.setDisplayName(bestallningSvar.getKlartext());
        return cvType;
    }
}
