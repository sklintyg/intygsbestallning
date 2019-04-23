package se.inera.intyg.intygsbestallning.web.version

import java.time.LocalDateTime

data class Versions(
   val applicationName: String? = null,
   val buildVersion: String? = null,
   val buildTimestamp: LocalDateTime? = null,
   val infraversion: String? = null
)
