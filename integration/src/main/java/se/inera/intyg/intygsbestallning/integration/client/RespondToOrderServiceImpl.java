package se.inera.intyg.intygsbestallning.integration.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.BestallningSvar;
import se.inera.intyg.intygsbestallning.common.dto.SimpleBestallningRequest;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.common.exception.IbServiceException;
import se.riv.intygsbestallning.certificate.order.respondtoorder.v1.rivtabp21.RespondToOrderResponderInterface;
import se.riv.intygsbestallning.certificate.order.respondtoorderresponder.v1.RespondToOrderResponseType;
import se.riv.intygsbestallning.certificate.order.respondtoorderresponder.v1.RespondToOrderType;
import se.inera.intyg.intygsbestallning.common.util.RivtaUtil;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.riv.intygsbestallning.certificate.order.v1.CVType;
import se.riv.intygsbestallning.certificate.order.v1.ResultCodeType;

import java.text.MessageFormat;

import static java.util.Objects.isNull;

@Service
public class RespondToOrderServiceImpl implements RespondToOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(RespondToOrderServiceImpl.class);

    private final RespondToOrderResponderInterface respondToOrderResponderInterface;
    private final IntegrationProperties integrationProperties;

    public RespondToOrderServiceImpl(
            RespondToOrderResponderInterface respondToOrderResponderInterface,
            IntegrationProperties integrationProperties) {
        this.respondToOrderResponderInterface = respondToOrderResponderInterface;
        this.integrationProperties = integrationProperties;
    }

    @Override
    public void sendRespondToOrder(SimpleBestallningRequest rRequest) {
        var respondToOrderType = createResponderType(rRequest);
        var response = respondToOrderResponderInterface.respondToOrder(integrationProperties.getSourceSystemHsaId(), respondToOrderType);
        handleResponse(response);
    }

    private void handleResponse(RespondToOrderResponseType response) {
        if (isNull(response)) {
            throw new IbServiceException(IbErrorCodeEnum.EXTERNAL_ERROR, "response was null");
        }
        if (isNull(response.getResult())) {
            throw new IbServiceException(IbErrorCodeEnum.EXTERNAL_ERROR, "response.Result was null");
        }
        if (isNull(response.getResult().getResultCode())) {
            throw new IbServiceException(IbErrorCodeEnum.EXTERNAL_ERROR, "response.Result.ResultCode was null");
        }

        ResultCodeType resultCode = response.getResult().getResultCode();
        if (resultCode.equals(ResultCodeType.ERROR)) {
            LOG.error(MessageFormat.format("RespondToOrder error '{0}'", response.getResult().getResultText()));
            throw new IbServiceException(IbErrorCodeEnum.EXTERNAL_ERROR, response.getResult().getResultText());
        }
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
