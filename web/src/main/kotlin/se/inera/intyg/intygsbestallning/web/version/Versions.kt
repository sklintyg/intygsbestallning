package se.inera.intyg.intygsbestallning.web.version

import java.time.LocalDateTime

data class VersionInfo(
   val applicationName: String,
   val buildVersion: String,
   val buildTimestamp: LocalDateTime,
   val activeProfiles: String
)
