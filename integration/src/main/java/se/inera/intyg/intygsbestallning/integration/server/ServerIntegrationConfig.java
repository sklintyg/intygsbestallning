package se.inera.intyg.intygsbestallning.integration.server;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.integration.server")
public class ServerIntegrationConfig {

    @Autowired
    private Bus bus;

    @Autowired
    private OrderAssessmentIntygsbestallning orderAssessment;

    @Bean
    public EndpointImpl orderAssessmentEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, orderAssessment);
        endpoint.publish("/order-assessment-responder");
        return endpoint;
    }
}
