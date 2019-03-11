package se.inera.intyg.intygsbestallning.common.domain

import java.time.LocalDateTime

data class Utredning(
   val id: Long? = null,
   val bestallning: Bestallning,
   val avslutDatum: LocalDateTime? = null,
   val vardenhet: Vardenhet
) {
  companion object Factory {
    fun newUtredning(hsaId: String, vardenhet: Vardenhet): Utredning {
      return Utredning(
         bestallning = Bestallning.newBestallning(hsaId = hsaId),
         vardenhet = vardenhet)
    }
  }
}
