package se.inera.intyg.intygsbestallning.common.domain

import java.time.LocalDateTime

data class Bestallning(
   val id: Long? = null,
   val intygTyp: String,
   val intygVersion: Double,
   val ankomstDatum: LocalDateTime,
   val avslutDatum: LocalDateTime? = null,
   val syfte: String? = null,
   val planeradeAktiviteter: String? = null,
   var status: BestallningStatus? = BestallningStatus.UNDEFINED,
   val invanare: Invanare,
   val handlaggare: Handlaggare,
   val vardenhet: Vardenhet,
   var handelser: List<Handelse>? = mutableListOf(),
   var notifieringar: List<Notifiering>? = mutableListOf()) {

  companion object Factory {
    fun newBestallning(
       invanare: Invanare,
       handlaggare: Handlaggare,
       intygTyp: String,
       intygVersion: Double,
       vardenhet: Vardenhet): Bestallning {
      return Bestallning(
         intygTyp = intygTyp,
         intygVersion = intygVersion,
         ankomstDatum = LocalDateTime.now(),
         invanare = invanare,
         handlaggare = handlaggare,
         vardenhet = vardenhet,
         handelser = listOf(Handelse.skapa()),
         notifieringar = listOf(Notifiering.nyBestallning(vardenhet.hsaId)))
    }
  }
}
