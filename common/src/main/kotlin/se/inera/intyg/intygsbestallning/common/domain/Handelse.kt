package se.inera.intyg.intygsbestallning.common.domain

import java.time.LocalDateTime

data class Handelse(
   val id: Long? = null,
   val event: BestallningEvent,
   val skapad: LocalDateTime,
   val anvandare: String? = null,
   val beskrivning: String,
   val kommentar: String? = null
) {
  companion object Factory {
    fun skapa(): Handelse {
      return Handelse(event = BestallningEvent.SKAPA, skapad = LocalDateTime.now(), beskrivning = "Beställning mottagen")
    }

    fun las(): Handelse {
      return Handelse(event = BestallningEvent.LAS, skapad = LocalDateTime.now(), beskrivning = "Beställning läst")
    }
  }
}
