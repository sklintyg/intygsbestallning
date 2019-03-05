package se.inera.intyg.intygsbestallning.integration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import se.inera.intyg.intygsbestallning.integration.client.ClientIntegrationConfig;
import se.inera.intyg.intygsbestallning.integration.server.ServerIntegrationConfig;

@Configuration
@Import({ClientIntegrationConfig.class, ServerIntegrationConfig.class, IntegrationProperties.class})
public class IntegrationConfig {
}
