package se.inera.intyg.intygsbestallning.common.domain

import java.time.LocalDateTime

data class Bestallning(
   val id: Long? = null,
   val intygTyp: String,
   val intygVersion: Double,
   val ankomstDatum: LocalDateTime,
   val avslutDatum: LocalDateTime? = null,
   var status: BestallningStatus? = BestallningStatus.UNDEFINED,
   val invanare: Invanare,
   val vardenhet: Vardenhet,
   var handelser: List<Handelse>? = mutableListOf(),
   var notifieringar: List<Notifiering>? = mutableListOf()) {

  companion object Factory {
    fun newBestallning(hsaId: String, invanare: Invanare, intygTyp: String, intygVersion: Double, vardenhet: Vardenhet): Bestallning {
      return Bestallning(
         intygTyp = intygTyp,
         intygVersion = intygVersion,
         ankomstDatum = LocalDateTime.now(),
         invanare = invanare,
         vardenhet = vardenhet,
         handelser = listOf(Handelse.skapa()),
         notifieringar = listOf(Notifiering.nyBestallning(hsaId)))
    }
  }
}
