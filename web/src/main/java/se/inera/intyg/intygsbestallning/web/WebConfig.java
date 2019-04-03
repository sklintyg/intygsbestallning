package se.inera.intyg.intygsbestallning.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import se.inera.intyg.infra.security.filter.PrincipalUpdatedFilter;
import se.inera.intyg.intygsbestallning.common.property.BestallningProperties;
import se.inera.intyg.intygsbestallning.web.controller.LocalDateTimeDeserializer;
import se.inera.intyg.intygsbestallning.web.controller.LocalDateTimeSerializer;
import se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning;
import se.inera.intyg.intygsbestallning.web.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static se.inera.intyg.intygsbestallning.web.controller.UserController.API_ANVANDARE;
import static se.inera.intyg.intygsbestallning.web.controller.UserController.API_UNIT_CONTEXT;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.web")
public class WebConfig implements WebMvcConfigurer {

    private Bus bus;
    private OrderAssessmentIntygsbestallning orderAssessment;

    private BestallningProperties bestallningProperties;

    public WebConfig(
            Bus bus,
            OrderAssessmentIntygsbestallning orderAssessment,
            BestallningProperties bestallningProperties) {
        super();
        this.bus = bus;
        this.orderAssessment = orderAssessment;
        this.bestallningProperties = bestallningProperties;
    }

    @Bean(name = "jacksonJsonProvider")
    public JacksonJaxbJsonProvider jacksonJsonProvider() {
        return new JacksonJaxbJsonProvider();
    }

    @Bean
    @Primary
    public ObjectMapper customObjectMapper() {

        SimpleModule module = new SimpleModule();
        module.addSerializer(new LocalDateTimeSerializer());

        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        return new Jackson2ObjectMapperBuilder()
                .indentOutput(true)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build();
    }

    @Bean
    public EndpointImpl orderAssessmentEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, orderAssessment);
        endpoint.publish("/order-assessment-responder");
        return endpoint;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        var path = bestallningProperties.getImageResourcePath();

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + path);
    }

    @Bean
    public PrincipalUpdatedFilter principalUpdatedFilter() {
        return new PrincipalUpdatedFilter();
    }

    @Bean
    @Autowired
    public FilterRegistrationBean<UnitContextSelectedAssuranceFilter> unitContextSelectedAssuranceFilter(UserService userService) {
        FilterRegistrationBean<UnitContextSelectedAssuranceFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new UnitContextSelectedAssuranceFilter(userService, List.of(API_ANVANDARE, API_UNIT_CONTEXT)));
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }


}
