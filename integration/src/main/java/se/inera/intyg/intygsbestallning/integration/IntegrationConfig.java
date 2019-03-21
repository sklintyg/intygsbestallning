package se.inera.intyg.intygsbestallning.integration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.inera.intyg.intygsbestallning.integration.client.ClientIntegrationConfig;
import se.inera.intyg.intygsbestallning.integration.hsa.HsaConfig;
import se.inera.intyg.intygsbestallning.integration.hsa.HsaStubConfig;
import se.inera.intyg.intygsbestallning.integration.pu.PuConfig;

@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.integration")
@Import({ClientIntegrationConfig.class,
        CacheConfigurationFromInfra.class,
        HsaConfig.class,
        PuConfig.class})
public class IntegrationConfig {
}
