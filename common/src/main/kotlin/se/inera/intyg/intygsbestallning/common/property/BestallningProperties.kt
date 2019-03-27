package se.inera.intyg.intygsbestallning.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "bestallning")
open class BestallningProperties {
  lateinit var textResourcePath: String
  lateinit var imageResourcePath: String
  lateinit var host: String
}
