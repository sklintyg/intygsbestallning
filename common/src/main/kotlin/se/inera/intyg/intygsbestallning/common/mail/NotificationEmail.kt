package se.inera.intyg.intygsbestallning.common.mail

data class NotificationEmail(
    var toAddress: String? = null,
    var subject: String? = null,
    var body: String? = null)
