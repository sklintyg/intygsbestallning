package se.inera.intyg.intygsbestallning.web.utredning;

import org.springframework.stereotype.Component;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentResponseType;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentType;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.rivtabp21.OrderAssessmentResponderInterface;
import se.riv.intygsbestallning.certificate.order.v1.CVType;
import se.riv.intygsbestallning.certificate.order.v1.CitizenType;
import se.riv.intygsbestallning.certificate.order.v1.IIType;
import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.CreateUtredningRequest;
import se.inera.intyg.intygsbestallning.common.IntygTyp;

@Component
public class OrderAssessmentIntygsbestallning implements OrderAssessmentResponderInterface {

    private CreateUtredningService createUtredningService;

    public OrderAssessmentIntygsbestallning(CreateUtredningService createUtredningService) {
        this.createUtredningService = createUtredningService;
    }

    @Override
    public OrderAssessmentResponseType orderAssessment(String logicalAddress, OrderAssessmentType orderAssessmentType) {
        var createUtredningRequest = fromType(orderAssessmentType);
        createUtredningService.createUtredning(createUtredningRequest);

        return null;
    }

    private CreateUtredningRequest fromType(OrderAssessmentType request) {

        if (request == null) {
            throw new IllegalArgumentException("request may not be null");
        }

        if (request.getAssessmentId() != null) {
            throw new IllegalArgumentException("assessmentId may not be set");
        }

        var personnummer = Optional.ofNullable(request.getCitizen())
                .map(CitizenType::getPersonalIdentity)
                .map(IIType::getExtension)
                .orElseThrow(() -> new IllegalArgumentException("extension may not be null"));

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

        return new CreateUtredningRequest(personnummer, intygTyp, vardenhet);
    }
}
