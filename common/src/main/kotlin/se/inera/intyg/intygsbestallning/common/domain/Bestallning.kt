package se.inera.intyg.intygsbestallning.common.domain

import java.time.LocalDateTime

data class Bestallning(
   val id: Long? = null,
   val ankomstDatum: LocalDateTime,
   var status: BestallningStatus? = BestallningStatus.UNDEFINED,
   var handelser: List<Handelse>? = mutableListOf(),
   var notifieringar: List<Notifiering>? = mutableListOf()) {

  companion object Factory {
    fun newBestallning(hsaId: String): Bestallning {
      return Bestallning(
         ankomstDatum = LocalDateTime.now(),
         handelser = listOf(Handelse.skapa()),
         notifieringar = listOf(Notifiering.inKommenBestallning(hsaId)))
    }
  }
}
