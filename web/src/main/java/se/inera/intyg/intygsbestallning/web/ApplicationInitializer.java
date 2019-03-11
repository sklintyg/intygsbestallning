package se.inera.intyg.intygsbestallning.web;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import se.inera.intyg.intygsbestallning.common.CommonConfig;
import se.inera.intyg.intygsbestallning.integration.CacheConfigurationFromInfra;
import se.inera.intyg.intygsbestallning.integration.IntegrationConfig;
import se.inera.intyg.intygsbestallning.integration.hsa.HsaConfig;
import se.inera.intyg.intygsbestallning.integration.pu.PuConfiguration;
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
                SecurityConfig.class);

                servletContext.addListener(new ContextLoaderListener(appContext));

        // CXF services filter
        ServletRegistration.Dynamic cxfServlet = servletContext.addServlet("services", new CXFServlet());
        cxfServlet.setLoadOnStartup(1);
        cxfServlet.addMapping("/services/*");
    }
}
