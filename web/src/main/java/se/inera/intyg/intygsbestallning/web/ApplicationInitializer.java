package se.inera.intyg.intygsbestallning.web;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.filter.DelegatingFilterProxy;
import se.inera.intyg.intygsbestallning.common.CommonConfig;
import se.inera.intyg.intygsbestallning.integration.IntegrationConfig;
import se.inera.intyg.intygsbestallning.mailsender.config.MailSenderConfig;
import se.inera.intyg.intygsbestallning.persistence.PersistenceConfig;
import se.inera.intyg.intygsbestallning.web.auth.BaseSecurityConfig;

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
                BaseSecurityConfig.class);

                servletContext.addListener(new ContextLoaderListener(appContext));

        // Spring security filter
        FilterRegistration.Dynamic springSecurityFilterChain = servletContext.addFilter("springSecurityFilterChain",
                DelegatingFilterProxy.class);
        springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");

        // unitSelectedAssurance filter
        FilterRegistration.Dynamic unitSelectedAssuranceFilter = servletContext.addFilter("unitSelectedAssuranceFilter",
                DelegatingFilterProxy.class);
        unitSelectedAssuranceFilter.setInitParameter("targetFilterLifecycle", "true");
        unitSelectedAssuranceFilter.addMappingForUrlPatterns(null, false, "/api/*");
        unitSelectedAssuranceFilter.setInitParameter("ignoredUrls", "/api/config,/api/user,/api/user/andraenhet");

        // CXF services filter
        ServletRegistration.Dynamic cxfServlet = servletContext.addServlet("services", new CXFServlet());
        cxfServlet.setLoadOnStartup(1);
        cxfServlet.addMapping("/services/*");
    }
}
