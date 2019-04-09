package se.inera.intyg.intygsbestallning.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import se.inera.intyg.intygsbestallning.common.CommonConfig;
import se.inera.intyg.intygsbestallning.integration.IntegrationConfig;
import se.inera.intyg.intygsbestallning.persistence.PersistenceConfig;
import se.inera.intyg.intygsbestallning.web.auth.SecurityConfig;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@Import({
        PersistenceConfig.class,
        CommonConfig.class,
        IntegrationConfig.class,
        WebConfig.class,
        SecurityConfig.class,
        SwaggerConfig.class
})
public class IntygsbestallningApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntygsbestallningApplication.class, args);
    }
}
