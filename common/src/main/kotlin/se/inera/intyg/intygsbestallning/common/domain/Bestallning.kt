package se.inera.intyg.intygsbestallning.common.domain

import java.time.LocalDateTime

data class Bestallning(
   val id: Long? = null,
   val intygTyp: IntygTyp,
   val ankomstDatum: LocalDateTime,
   val avslutDatum: LocalDateTime? = null,
   var status: BestallningStatus? = BestallningStatus.UNDEFINED,
   val invanare: Invanare,
   val vardenhet: Vardenhet,
   var handelser: List<Handelse>? = mutableListOf(),
   var notifieringar: List<Notifiering>? = mutableListOf()) {

  companion object Factory {
    fun newBestallning(hsaId: String, invanare: Invanare, intygTyp: IntygTyp, vardenhet: Vardenhet): Bestallning {
      return Bestallning(
         intygTyp = intygTyp,
         ankomstDatum = LocalDateTime.now(),
         invanare = invanare,
         vardenhet = vardenhet,
         handelser = listOf(Handelse.skapa()),
         notifieringar = listOf(Notifiering.nyBestallning(hsaId)))
    }
  }
}
