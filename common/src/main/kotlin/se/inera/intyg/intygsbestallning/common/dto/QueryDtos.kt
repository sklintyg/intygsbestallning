package se.inera.intyg.intygsbestallning.common.dto

import se.inera.intyg.intygsbestallning.common.domain.Bestallning
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus
import se.inera.intyg.intygsbestallning.common.domain.IntygTyp
import se.inera.intyg.intygsbestallning.common.domain.Invanare
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter
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
   val intygTyp: String,
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

data class VisaBestallningDto(
   val id: Long,
   val status: String,
   val ankomstDatum: LocalDate,
   val intygTyp: String,
   val invanare: BestallningInvanareDto,
   val fragor: List<Fraga>
) {
  companion object Factory {
    fun toDto(bestallning: Bestallning, bestallningTexter: BestallningTexter): VisaBestallningDto {

      val textMap = bestallningTexter.texter.map { it.id to it.value }.toMap()

      return VisaBestallningDto(
         id = bestallning.id!!,
         status = bestallning.status!!.name,
         ankomstDatum = bestallning.ankomstDatum.toLocalDate(),
         intygTyp = bestallning.intygTyp,
         invanare = BestallningInvanareDto.toDto(bestallning.invanare),
         fragor = listOf(
            Fraga(
               rubrik = textMap.getValue(RBK_1),
               delfragor = listOf(
                  Delfraga(etikett = textMap.getValue(ETK_1_1), bild = ""),
                  Delfraga(etikett = textMap.getValue(ETK_1_2), text = textMap.getValue(TEXT_1_2_1)),
                  Delfraga(etikett = textMap.getValue(ETK_1_3), text = textMap.getValue(TEXT_1_3_1)))
            ),
            Fraga(
               rubrik = textMap.getValue(RBK_2),
               delfragor = listOf(
                  Delfraga(etikett = textMap.getValue(ETK_2_1), svar = bestallning.invanare.personId.personnummerWithDash),
                  Delfraga(etikett = textMap.getValue(ETK_2_2), svar = listOfNotNull(
                     bestallning.invanare.fornamn,
                     bestallning.invanare.mellannamn,
                     bestallning.invanare.efternamn).joinToString(separator = " ")))
            ),
            Fraga(
               rubrik = textMap.getValue(RBK_3),
               delfragor = listOf(
                  Delfraga(etikett = textMap.getValue(ETK_3_1), svar = "ett syfte"), //TODO bestallning.syfte
                  Delfraga(etikett = textMap.getValue(ETK_3_2), svar = bestallning.invanare.bakgrundNulage),
                  Delfraga(etikett = textMap.getValue(ETK_3_3), svar = "insatser ska göras")) //TODO: bestallning.planeradeInstatser
            ),
            Fraga(
               rubrik = textMap.getValue(RBK_5),
               delfragor = listOf(
                  Delfraga(etikett = textMap.getValue(ETK_5_1), svar = listOfNotNull(
                     "kontoret",
                     "hand@hand.se",
                     "08-08080808").joinToString(separator = "\n")),
                  Delfraga(etikett = textMap.getValue(ETK_5_2), svar = listOfNotNull(
                     "NamnKontoret",
                     "AdressKontoret").joinToString(separator = "\n"))
               )
            ),
            Fraga(
               rubrik = textMap.getValue(RBK_6),
               delfragor = listOf(
                  Delfraga(etikett = textMap.getValue(ETK_6_1), text = textMap.getValue(TEXT_6_1_1)),
                  Delfraga(etikett = textMap.getValue(ETK_6_2), svar = bestallning.id.toString()),
                  Delfraga(etikett = textMap.getValue(ETK_6_3), svar = "Kostnadsställe"),
                  Delfraga(etikett = textMap.getValue(ETK_6_4), text = textMap.getValue(TEXT_6_4_1)),
                  Delfraga(etikett = textMap.getValue(ETK_6_5), text = textMap.getValue(TEXT_6_5_1)))
            )
         )
      )
    }
  }
}

data class Fraga(
   val rubrik: String,
   val delfragor: List<Delfraga>
)

data class Delfraga(
   val etikett: String,
   val text: String? = null,
   val bild: String? = null,
   val svar: String? = null
)


//Forfragan
const val RBK_1: String = "RBK_1"
const val ETK_1_1: String = "ETK_1.1"
const val ETK_1_2: String = "ETK_1.2"
const val TEXT_1_2_1: String = "TEXT_1.2.1"
const val ETK_1_3: String = "ETK_1.3"
const val TEXT_1_3_1: String = "TEXT_1.3.1"
const val RBK_2: String = "RBK_2"
const val ETK_2_1: String = "ETK_2.1"
const val ETK_2_2: String = "ETK_2.2"
const val RBK_3: String = "RBK_3"
const val ETK_3_1: String = "ETK_3.1"
const val ETK_3_2: String = "ETK_3.2"
const val ETK_3_3: String = "ETK_3.3"
const val RBK_5: String = "RBK_5"
const val ETK_5_1: String = "ETK_5.1"
const val ETK_5_2: String = "ETK_5.2"

//Faktureringsunderlag
const val RBK_6: String = "RBK_6"
const val ETK_6_1: String = "ETK_6.1"
const val TEXT_6_1_1: String = "TEXT_6.1.1"
const val ETK_6_2: String = "ETK_6.2"
const val ETK_6_3: String = "ETK_6.3"
const val ETK_6_4: String = "ETK_6.4"
const val TEXT_6_4_1: String = "TEXT_6.4.1"
const val ETK_6_5: String = "ETK_6.5"
const val TEXT_6_5_1: String = "TEXT_6.5.1"
