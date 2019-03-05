package se.inera.intyg.intygsbestallning.integration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "integration")
open class IntegrationProperties {
  lateinit var myndighetIntegrationUrl: String
  lateinit var respondToOrderUrl: String
}
