package se.inera.intyg.intygsbestallning.integration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import se.inera.intyg.intygsbestallning.integration.client.ClientIntegrationConfig;
import se.inera.intyg.intygsbestallning.integration.hsa.HsaConfig;

@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.integration")
@Import({ClientIntegrationConfig.class, IntegrationProperties.class, CacheConfigurationFromInfra.class, HsaConfig.class})
public class IntegrationConfig {
}
