package se.inera.intyg.intygsbestallning.web.service.bestallning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentResponseType;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentType;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.rivtabp21.OrderAssessmentResponderInterface;
import se.riv.intygsbestallning.certificate.order.v1.CVType;
import se.riv.intygsbestallning.certificate.order.v1.CitizenType;
import se.riv.intygsbestallning.certificate.order.v1.IIType;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.domain.IntygTyp;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.inera.intyg.intygsbestallning.common.util.RivtaUtil;

@Component
public class OrderAssessmentIntygsbestallning implements OrderAssessmentResponderInterface {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private CreateBestallningService createBestallningService;
    private IntegrationProperties integrationProperties;

    public OrderAssessmentIntygsbestallning(CreateBestallningService createBestallningService) {
        this.createBestallningService = createBestallningService;
    }

    @Override
    public OrderAssessmentResponseType orderAssessment(String logicalAddress, OrderAssessmentType orderAssessmentType) {

        LOG.info("Received request for OrderAssessment");

        try {
            var createBestallningRequest = fromType(orderAssessmentType);
            createBestallningService.create(createBestallningRequest);
        } catch (final Exception e) {
            LOG.error("Error in orderAssessment", e);
            OrderAssessmentResponseType response = new OrderAssessmentResponseType();
            response.setAssessmentId(RivtaUtil.anII(integrationProperties.getSourceSystemHsaId(), ""));
            response.setResult(RivtaUtil.aResultTypeError(e));
            return response;
        }

        return null;
    }

    private CreateBestallningRequest fromType(OrderAssessmentType request) {
        if (request == null) {
            throw new IllegalArgumentException("request may not be null");
        }

        if (request.getAssessmentId() != null) {
            throw new IllegalArgumentException("assessmentId may not be set");
        }

        var personnummer = Optional.ofNullable(request.getCitizen())
                .map(CitizenType::getPersonalIdentity)
                .map(IIType::getExtension)
                .orElseThrow(() -> new IllegalArgumentException("personummer may not be null"));

        var vardenhet = Optional.ofNullable(request.getCareUnitId())
                .map(IIType::getExtension)
                .orElseThrow(() -> new IllegalArgumentException("careUnitId may not be null"));

        var certficateCodeType = Optional.ofNullable(request.getCertificateType())
                .map(CVType::getCode)
                .orElseThrow(() -> new IllegalArgumentException("certificateType may not be null"));

        var intygTyp = IntygTyp.valueOf(certficateCodeType);

        if (!intygTyp.getBestallningbar()) {
            throw new IllegalArgumentException("certificateType: " + certficateCodeType + " is not bestallningbar");
        }

        return new CreateBestallningRequest(personnummer, intygTyp, vardenhet);
    }
}
