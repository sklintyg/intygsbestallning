package se.inera.intyg.intygsbestallning.common.dto

import se.inera.intyg.intygsbestallning.common.domain.Bestallning
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus
import se.inera.intyg.intygsbestallning.common.domain.IntygTyp
import se.inera.intyg.intygsbestallning.common.domain.Invanare
import java.time.LocalDate

data class ListBestallningarQuery(
   val statusar: List<BestallningStatus>,
   val textSearch: String?,
   val pageIndex: Int,
   val limit: Int)

data class ListBestallningarResult(
   val bestallningar: List<ListBestallningDto>,
   val pageIndex: Int? = 0,
   val totalPages: Int,
   val numberOfElements: Int,
   val totalElements: Long
) {
  companion object Factory {
    fun toDto(
       bestallningar: List<Bestallning>,
       pageIndex: Int? = 0,
       totalPages: Int,
       numberOfElements: Int,
       totalElements: Long): ListBestallningarResult {
      return ListBestallningarResult(
         bestallningar = bestallningar.map { ListBestallningDto.toDto(it) },
         pageIndex = pageIndex,
         totalPages = totalPages,
         numberOfElements = numberOfElements,
         totalElements = totalElements)
    }
  }
}

data class ListBestallningDto(
   val id: Long,
   val status: String,
   val ankomstDatum: LocalDate,
   val intygTyp: IntygTyp,
   val invanare: ListBestallningInvanareDto
) {
  companion object Factory {
    fun toDto(bestallning: Bestallning): ListBestallningDto {
      return ListBestallningDto(
         id = bestallning.id!!,
         status = bestallning.status!!.beskrivning,
         ankomstDatum = bestallning.ankomstDatum.toLocalDate(),
         intygTyp = bestallning.intygTyp,
         invanare = ListBestallningInvanareDto.toDto(bestallning.invanare)
      )
    }
  }
}

data class ListBestallningInvanareDto(
   val personId: String,
   val name: String
) {
  companion object Factory {
    fun toDto(invanare: Invanare): ListBestallningInvanareDto {
      return ListBestallningInvanareDto(
         personId = invanare.personId.personnummerWithDash,
         name = listOf(invanare.fornamn, invanare.mellannamn, invanare.efternamn).joinToString(separator = " ")
      )
    }
  }
}
