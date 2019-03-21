package se.inera.intyg.intygsbestallning.web.bestallning

import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus.*

data class AccepteraBestallning(val fritextForklaring: String? = null)

enum class BestallningStatusKategori(val list: List<BestallningStatus>) {
  AKTUELLA(listOf(OLAST, LAST, ACCEPTERAD)),
  KLARA(listOf(KLAR)),
  AVVISADE(listOf(AVVISAD, RADERAD))
}
