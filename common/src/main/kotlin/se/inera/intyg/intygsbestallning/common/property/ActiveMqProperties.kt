package se.inera.intyg.intygsbestallning.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "activemq.broker")
open class ActiveMqProperties {
  lateinit var url: String
  var password: String? = null
  var username: String? = null
}
