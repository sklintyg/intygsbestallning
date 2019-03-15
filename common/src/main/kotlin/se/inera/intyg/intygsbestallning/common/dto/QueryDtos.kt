package se.inera.intyg.intygsbestallning.common.dto

import se.inera.intyg.intygsbestallning.common.domain.Bestallning
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus

data class ListBestallningarQuery(
   val statusar: List<BestallningStatus>,
   val textSearch: String?,
   val pageIndex: Int,
   val limit: Int)

data class ListBestallningarResult(
   val bestallningar: List<Bestallning>,
   val pageIndex: Int? = 0,
   val totalPages: Int,
   val numberOfElements: Int,
   val totalElements: Long
)
