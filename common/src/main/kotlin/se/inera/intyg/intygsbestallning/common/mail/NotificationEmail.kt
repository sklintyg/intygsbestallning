package se.inera.intyg.intygsbestallning.common.mail

import se.inera.intyg.intygsbestallning.common.text.mail.MailTexter

data class NotificationEmail(
   var toAddress: String? = null,
   var subject: String? = null,
   var body: String? = null
)

data class MailContent(
   val hostUrl: String,
   val texter: MailTexter,
   val url: String
)
