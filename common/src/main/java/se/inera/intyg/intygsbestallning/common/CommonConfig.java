package se.inera.intyg.intygsbestallning.common;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.common")
@EnableConfigurationProperties
@Import({ IntegrationProperties.class })
public class CommonConfig {
}
