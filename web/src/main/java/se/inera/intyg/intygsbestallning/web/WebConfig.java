package se.inera.intyg.intygsbestallning.web;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import java.time.LocalDateTime;
import se.inera.intyg.intygsbestallning.web.controller.LocalDateTimeDeserializer;
import se.inera.intyg.intygsbestallning.web.controller.LocalDateTimeSerializer;
import se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.web")
public class WebConfig {

    private Bus bus;
    private OrderAssessmentIntygsbestallning orderAssessment;

    public WebConfig(Bus bus, OrderAssessmentIntygsbestallning orderAssessment) {
        this.bus = bus;
        this.orderAssessment = orderAssessment;
    }

    @Bean(name = "jacksonJsonProvider")
    public JacksonJaxbJsonProvider jacksonJsonProvider() {
        return new JacksonJaxbJsonProvider();
    }

    @Bean
    @Primary
    public ObjectMapper customObjectMapper(){

        SimpleModule module = new SimpleModule();
        module.addSerializer(new LocalDateTimeSerializer());

        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        return new Jackson2ObjectMapperBuilder()
                .indentOutput(true)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    @Bean
    public EndpointImpl orderAssessmentEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, orderAssessment);
        endpoint.publish("/order-assessment-responder");
        return endpoint;
    }
}
