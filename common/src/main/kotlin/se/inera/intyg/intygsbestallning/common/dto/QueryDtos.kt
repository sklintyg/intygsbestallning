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
   val limit: Int,
   val sortableColumn: ListBestallningSortColumn,
   val sortDirection: ListBestallningDirection)

enum class ListBestallningSortColumn(val kolumn: String) {
  ID("id"),
  INTYG_TYP("intygTyp"),
  ANKOMST_DATUM("ankomstDatum"),
  STATUS("status"),
  INVANARE_PERSON_ID("invanare.personId")
}

enum class ListBestallningDirection {
  DESC,
  ASC
}

data class ListBestallningarResult(
   val bestallningar: List<BestallningDto>,
   val pageIndex: Int? = 0,
   val totalPages: Int,
   val numberOfElements: Int,
   val totalElements: Long,
   val sortableColumn: ListBestallningSortColumn,
   val sortDirection: ListBestallningDirection
) {
  companion object Factory {
    fun toDto(
       bestallningar: List<Bestallning>,
       pageIndex: Int? = 0,
       totalPages: Int,
       numberOfElements: Int,
       totalElements: Long,
       sortableColumn: ListBestallningSortColumn,
       sortDirection: ListBestallningDirection): ListBestallningarResult {
      return ListBestallningarResult(
         bestallningar = bestallningar.map { BestallningDto.toDto(it) },
         pageIndex = pageIndex,
         totalPages = totalPages,
         numberOfElements = numberOfElements,
         totalElements = totalElements,
         sortableColumn = sortableColumn,
         sortDirection = sortDirection)
    }
  }
}

data class BestallningDto(
   val id: Long,
   val status: String,
   val ankomstDatum: LocalDate,
   val intygTyp: IntygTyp,
   val invanare: BestallningInvanareDto
) {
  companion object Factory {
    fun toDto(bestallning: Bestallning): BestallningDto {
      return BestallningDto(
         id = bestallning.id!!,
         status = bestallning.status!!.beskrivning,
         ankomstDatum = bestallning.ankomstDatum.toLocalDate(),
         intygTyp = bestallning.intygTyp,
         invanare = BestallningInvanareDto.toDto(bestallning.invanare)
      )
    }
  }
}

data class BestallningInvanareDto(
   val personId: String,
   val name: String
) {
  companion object Factory {
    fun toDto(invanare: Invanare): BestallningInvanareDto {
      return BestallningInvanareDto(
         personId = invanare.personId.personnummerWithDash,
         name = listOf(invanare.fornamn, invanare.mellannamn, invanare.efternamn).joinToString(separator = " ")
      )
    }
  }
}

data class AktuellBestallningDto(
   val bestallning: BestallningDto,
   val test: String = "hej"
) {
  companion object Factory {
    fun toDto(bestallning: Bestallning): AktuellBestallningDto {
      return AktuellBestallningDto(BestallningDto.toDto(bestallning))
    }
  }
}
