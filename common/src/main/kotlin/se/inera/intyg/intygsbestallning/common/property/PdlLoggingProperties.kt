package se.inera.intyg.intygsbestallning.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "pdl")
open class PdlLoggingProperties {
  lateinit var systemId: String
  lateinit var systemName: String
}
