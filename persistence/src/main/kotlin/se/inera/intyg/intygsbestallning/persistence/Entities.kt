package se.inera.intyg.intygsbestallning.persistence

import java.time.LocalDateTime

data class Bestallning(
   val id: String,
   val skapatDatum: LocalDateTime
)
