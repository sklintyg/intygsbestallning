package se.inera.intyg.intygsbestallning.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "mailsender")
open class MailSenderProperties {
  lateinit var maximumRedeliveries: Integer
  lateinit var redeliveryDelay: Integer
  lateinit var backOffMultiplier: Integer
  lateinit var queueName: String
}
