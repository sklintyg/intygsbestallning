package se.inera.intyg.intygsbestallning.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ConfigurationProperties(prefix = "integration")
open class IntegrationProperties {
  lateinit var myndighetIntegrationUrl: String
  lateinit var respondToOrderUrl: String
  lateinit var codeSystemCertificateType: String
  lateinit var sourceSystemHsaId: String
}
