/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygsbestallning.web.config;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.cxf.Bus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.feature.transform.XSLTOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import se.inera.intyg.infra.monitoring.logging.LogMDCServletFilter;
import se.inera.intyg.infra.security.filter.PrincipalUpdatedFilter;
import se.inera.intyg.infra.security.filter.RequestContextHolderUpdateFilter;
import se.inera.intyg.infra.security.filter.SecurityHeadersFilter;
import se.inera.intyg.infra.security.filter.SessionTimeoutFilter;
import se.inera.intyg.intygsbestallning.web.UnitContextSelectedAssuranceFilter;
import se.inera.intyg.intygsbestallning.web.controller.LocalDateTimeDeserializer;
import se.inera.intyg.intygsbestallning.web.controller.LocalDateTimeSerializer;
import se.inera.intyg.intygsbestallning.web.interceptor.IbSoapFaultInterceptor;
import se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning;
import se.inera.intyg.intygsbestallning.web.service.user.UserService;

import static se.inera.intyg.intygsbestallning.web.controller.SessionStatController.SESSION_STATUS_CHECK_URI;
import static se.inera.intyg.intygsbestallning.web.controller.UserController.API_ANVANDARE;
import static se.inera.intyg.intygsbestallning.web.controller.UserController.API_UNIT_CONTEXT;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.web")
public class WebConfig implements WebMvcConfigurer {

    private static final int LOGGING_LIMIT = 1024 * 1024;

    private Bus bus;
    private OrderAssessmentIntygsbestallning orderAssessment;

    public WebConfig(Bus bus, OrderAssessmentIntygsbestallning orderAssessment) {
        super();
        this.bus = bus;
        this.orderAssessment = orderAssessment;
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
        endpoint.getOutFaultInterceptors().add(soapInterceptor());
        endpoint.getFeatures().add(loggingFeature());
        endpoint.publish("/order-assessment-responder");
        return endpoint;
    }

    @Bean
    public XSLTOutInterceptor soapInterceptor() {
        return new IbSoapFaultInterceptor("transform/order-assessment.xslt");
    }

    @Bean
    public FilterRegistrationBean<RequestContextHolderUpdateFilter> requestContextHolderUpdateFilter() {
        FilterRegistrationBean<RequestContextHolderUpdateFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RequestContextHolderUpdateFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER + 3);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<LogMDCServletFilter> logMDCServletFilter() {
        FilterRegistrationBean<LogMDCServletFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new LogMDCServletFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER + 2);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<SessionTimeoutFilter> sessionTimeoutFilter() {
        final SessionTimeoutFilter sessionTimeoutFilter = new SessionTimeoutFilter();
        sessionTimeoutFilter.setGetSessionStatusUri(SESSION_STATUS_CHECK_URI);

        FilterRegistrationBean<SessionTimeoutFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(sessionTimeoutFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER + 1);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<PrincipalUpdatedFilter> principalUpdatedFilter() {
        FilterRegistrationBean<PrincipalUpdatedFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new PrincipalUpdatedFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 1);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<UnitContextSelectedAssuranceFilter> unitContextSelectedAssuranceFilter(UserService userService) {
        FilterRegistrationBean<UnitContextSelectedAssuranceFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new UnitContextSelectedAssuranceFilter(userService, List.of(API_ANVANDARE, API_UNIT_CONTEXT)));
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 2);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> characterEncodingFilter() {
        FilterRegistrationBean<CharacterEncodingFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new CharacterEncodingFilter("UTF-8", true));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 3);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter() {
        FilterRegistrationBean<HiddenHttpMethodFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new HiddenHttpMethodFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<SecurityHeadersFilter> securityHeadersFilter() {
        FilterRegistrationBean<SecurityHeadersFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new SecurityHeadersFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    private LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setLimit(LOGGING_LIMIT); // Size of the log message before it is truncated
        loggingFeature.setPrettyLogging(true);

        return loggingFeature;
    }
}
