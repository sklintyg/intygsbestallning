package se.inera.intyg.intygsbestallning.integration;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.integration")
public class IntegrationConfig {


    @Autowired
    private Bus bus;

    @Autowired
    private OrderAssessmentResponderIntygsbestallning orderAssessmentResponder;

    @Bean
    public EndpointImpl orderAssessmentResponderEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, orderAssessmentResponder);
        endpoint.publish("/order-assessment-responder");
        return endpoint;
    }
}
