package se.inera.intyg.intygsbestallning.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "persistence.bootstrap")
open class PersistenceProperties {
  lateinit var invanareDirectory: String
  lateinit var vardenhetDirectory: String
  lateinit var bestallningDirectory: String
}
