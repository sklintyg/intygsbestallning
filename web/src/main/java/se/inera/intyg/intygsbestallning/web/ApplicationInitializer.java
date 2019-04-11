package se.inera.intyg.intygsbestallning.web;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import se.inera.intyg.infra.security.filter.RequestContextHolderUpdateFilter;
import se.inera.intyg.infra.security.filter.SecurityHeadersFilter;
import se.inera.intyg.intygsbestallning.common.CommonConfig;
import se.inera.intyg.intygsbestallning.integration.IntegrationConfig;
import se.inera.intyg.intygsbestallning.mailsender.config.MailSenderConfig;
import se.inera.intyg.intygsbestallning.persistence.PersistenceConfig;
import se.inera.intyg.intygsbestallning.web.auth.SecurityConfig;

public class ApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();

        appContext.register(
                IntegrationConfig.class,
                CommonConfig.class,
                MailSenderConfig.class,
                PersistenceConfig.class,
                WebConfig.class,
                SwaggerConfig.class,
                SecurityConfig.class);

        servletContext.addListener(new ContextLoaderListener(appContext));

        AnnotationConfigWebApplicationContext webConfig = new AnnotationConfigWebApplicationContext();
        webConfig.register(WebConfig.class);
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(webConfig));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");

        // Spring session filter
        FilterRegistration.Dynamic springSessionRepositoryFilter = servletContext.addFilter("springSessionRepositoryFilter",
                DelegatingFilterProxy.class);
        springSessionRepositoryFilter.addMappingForUrlPatterns(null, false, "/*");

        // Update RequestContext with spring session
        FilterRegistration.Dynamic requestContextHolderUpdateFilter = servletContext.addFilter("requestContextHolderUpdateFilter",
                RequestContextHolderUpdateFilter.class);
        requestContextHolderUpdateFilter.addMappingForUrlPatterns(null, false, "/*");

        // LogMDCServletFilter
        FilterRegistration.Dynamic logMdcFilter = servletContext.addFilter("logMDCServletFilter",
                DelegatingFilterProxy.class);
        logMdcFilter.addMappingForUrlPatterns(null, false, "/*");

        // Spring security filter
        FilterRegistration.Dynamic springSecurityFilterChain = servletContext.addFilter("springSecurityFilterChain",
                DelegatingFilterProxy.class);
        springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");

        // principalUpdatedFilter filter
        FilterRegistration.Dynamic principalUpdatedFilter = servletContext.addFilter("principalUpdatedFilter",
                DelegatingFilterProxy.class);
        principalUpdatedFilter.setInitParameter("targetFilterLifecycle", "true");
        principalUpdatedFilter.addMappingForUrlPatterns(null, false, "/*");


        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("characterEncodingFilter",
                CharacterEncodingFilter.class);
        characterEncodingFilter.addMappingForUrlPatterns(null, false, "/*");
        characterEncodingFilter.setInitParameter("encoding", "UTF-8");
        characterEncodingFilter.setInitParameter("forceEncoding", "true");

        FilterRegistration.Dynamic hiddenHttpMethodFilter = servletContext.addFilter("hiddenHttpMethodFilter",
                HiddenHttpMethodFilter.class);
        hiddenHttpMethodFilter.addMappingForUrlPatterns(null, false, "/*");

        FilterRegistration.Dynamic securityHeadersFilter = servletContext.addFilter("securityHeadersFilter",
                SecurityHeadersFilter.class);
        securityHeadersFilter.addMappingForUrlPatterns(null, true, "/*");

    }
}
