package se.inera.intyg.intygsbestallning.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "mailsender")
open class MailSenderProperties {
  var maximumRedeliveries: Int? = 0
  var redeliveryDelay: Int? = 0
  var backOffMultiplier: Int? = 0
  var queueName: String? = ""
}
