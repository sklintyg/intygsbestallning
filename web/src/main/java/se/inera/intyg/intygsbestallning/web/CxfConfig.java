package se.inera.intyg.intygsbestallning.web;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.inera.intyg.intygsbestallning.web.utredning.OrderAssessmentIntygsbestallning;

@Configuration
public class CxfConfig {

    private Bus bus;
    private OrderAssessmentIntygsbestallning orderAssessment;

    public CxfConfig(Bus bus, OrderAssessmentIntygsbestallning orderAssessment) {
        this.bus = bus;
        this.orderAssessment = orderAssessment;
    }

    @Bean
    public EndpointImpl orderAssessmentEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, orderAssessment);
        endpoint.publish("/order-assessment-responder");
        return endpoint;
    }
}
