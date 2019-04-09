package se.inera.intyg.intygsbestallning.common;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import se.inera.intyg.intygsbestallning.common.property.ActiveMqProperties;
import se.inera.intyg.intygsbestallning.common.property.BestallningProperties;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.inera.intyg.intygsbestallning.common.property.MailProperties;
import se.inera.intyg.intygsbestallning.common.property.MailSenderProperties;
import se.inera.intyg.intygsbestallning.common.property.PdlLoggingProperties;
import se.inera.intyg.intygsbestallning.common.property.PersistenceProperties;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.common")
@EnableConfigurationProperties
@Import({
        BestallningProperties.class,
        IntegrationProperties.class,
        MailProperties.class,
        MailSenderProperties.class,
        PdlLoggingProperties.class,
        PersistenceProperties.class,
        ActiveMqProperties.class
})
public class CommonConfig {
}
