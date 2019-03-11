package se.inera.intyg.intygsbestallning.integration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import se.inera.intyg.intygsbestallning.integration.client.ClientIntegrationConfig;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.integration")
@Import({ClientIntegrationConfig.class})
public class IntegrationConfig {
}
