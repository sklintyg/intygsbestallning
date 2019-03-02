package se.inera.intyg.intygsbestallning.integration.responders;

import org.springframework.stereotype.Component;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentResponseType;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentType;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.rivtabp21.OrderAssessmentResponderInterface;

@Component
public class OrderAssessmentResponderIntygsbestallning implements OrderAssessmentResponderInterface {

    @Override
    public OrderAssessmentResponseType orderAssessment(String logicalAddress, OrderAssessmentType orderAssessmentType) {
        throw new IllegalArgumentException("Not implemented");
    }
}
