package se.inera.intyg.intygsbestallning.web;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import se.inera.intyg.intygsbestallning.web.service.utredning.OrderAssessmentIntygsbestallning;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.web")
public class WebConfig {

    private Bus bus;
    private OrderAssessmentIntygsbestallning orderAssessment;

    public WebConfig(Bus bus, OrderAssessmentIntygsbestallning orderAssessment) {
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