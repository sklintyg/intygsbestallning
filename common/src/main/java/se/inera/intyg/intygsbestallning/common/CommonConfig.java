package se.inera.intyg.intygsbestallning.common;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.inera.intyg.intygsbestallning.common.property.MailProperties;
import se.inera.intyg.intygsbestallning.common.property.PdlLoggingProperties;
import se.inera.intyg.intygsbestallning.common.property.PersistenceProperties;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.common")
@EnableConfigurationProperties
@Import({
        IntegrationProperties.class,
        MailProperties.class,
        PdlLoggingProperties.class,
        PersistenceProperties.class
})
public class CommonConfig {
}
