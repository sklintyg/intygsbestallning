/**
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygsbestallning.common.dto

import se.inera.intyg.intygsbestallning.common.domain.Bestallning
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus
import se.inera.intyg.intygsbestallning.common.domain.Invanare
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter
import java.time.LocalDate
import java.util.*

data class CountBestallningarQuery(
   val statusar: List<BestallningStatus>,
   val hsaId: String,
   val orgNrVardgivare: String)

data class StatResponse(
   val antalOlastaBestallningar: Long,
   val antalAktivaBestallningar: Long,
   val antalKlaraBestallningar: Long)

data class ListBestallningarBasedOnStatusQuery(
   val statusar: List<BestallningStatus>)

data class ListBestallningarQuery(
   val statusar: List<BestallningStatus>,
   val hsaId: String,
   val orgNrVardgivare: String,
   val textSearch: String?,
   val pageIndex: Int,
   val limit: Int,
   val sortColumn: ListBestallningSortColumn,
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

data class PageDto(
   val pageIndex: Int? = 0,
   val start: Int,
   val end: Int,
   val totalPages: Int,
   val numberOfElements: Int,
   val totalElements: Long
)

data class ListBestallningarResult(
   val ingaRegistreradeBestallningar: Boolean,
   val bestallningar: List<ListBestallningDto>,
   val pageIndex: Int? = 0,
   val start: Int,
   val end: Int,
   val totalPages: Int,
   val numberOfElements: Int,
   val totalElements: Long,
   val sortColumn: ListBestallningSortColumn,
   val sortDirection: ListBestallningDirection
) {
  companion object Factory {
    fun toDto(
       isFritextSok: Boolean,
       bestallningar: List<Bestallning>,
       pageDto: PageDto,
       sortColumn: ListBestallningSortColumn,
       sortDirection: ListBestallningDirection): ListBestallningarResult {
      return ListBestallningarResult(
         ingaRegistreradeBestallningar = !isFritextSok && bestallningar.isEmpty(),
         bestallningar = bestallningar.map { ListBestallningDto.toDto(it) },
         pageIndex = pageDto.pageIndex,
         start = pageDto.start,
         end = pageDto.end,
         totalPages = pageDto.totalPages,
         numberOfElements = pageDto.numberOfElements,
         totalElements = pageDto.totalElements,
         sortColumn = sortColumn,
         sortDirection = sortDirection)
    }
  }
}

data class ListBestallningDto(
   val id: Long,
   val status: String,
   val ankomstDatum: LocalDate,
   val intygTyp: String,
   val invanare: ListBestallningInvanareDto
) {
  companion object Factory {
    fun toDto(bestallning: Bestallning): ListBestallningDto {
      return ListBestallningDto(
         id = bestallning.id!!,
         status = bestallning.status!!.beskrivning,
         ankomstDatum = bestallning.ankomstDatum.toLocalDate(),
         intygTyp = bestallning.intygTyp,
         invanare = ListBestallningInvanareDto(bestallning.invanare.personId.personnummerWithDash)
      )
    }
  }
}

data class ListBestallningInvanareDto(
   val personId: String
)

data class BestallningInvanareDto(
   val personId: String,
   val sekretessMarkering: Boolean,
   val name: String,
   val headerName: String?
) {
  companion object Factory {
    fun toDto(
       invanare: Invanare,
       fornamn: String,
       mellannamn: String?,
       efternamn: String,
       sekretessMarkering: Boolean): BestallningInvanareDto {
      return BestallningInvanareDto(
         personId = invanare.personId.personnummerWithDash,
         sekretessMarkering = sekretessMarkering,
         name =
         if (!sekretessMarkering)
           listOfNotNull(
              fornamn,
              mellannamn,
              efternamn).joinToString(separator = " ")
         else "Sekretessmarkerad uppgift",
         headerName =
         if (!sekretessMarkering)
           listOfNotNull(
              fornamn,
              efternamn).joinToString(separator = " ")
         else null
      )
    }
  }
}

data class VisaBestallningMetadata(
   val bild: String,
   val metaData: List<BestallningMetaData>,
   val scope: VisaBestallningScope
)

enum class VisaBestallningScope {
  ALL, FORFRAGAN, FAKTURERINGSUNDERLAG
}

data class VisaBestallningDto(
   val id: Long,
   val status: String,
   val ankomstDatum: LocalDate,
   val intygTyp: String,
   val intygTypBeskrivning: String,
   val invanare: BestallningInvanareDto,
   val metaData: List<BestallningMetaData>,
   val fragor: List<Fraga>
) {
  companion object Factory {
    fun toDto(
       bestallning: Bestallning,
       invanareDto: BestallningInvanareDto,
       bestallningTexter: BestallningTexter,
       metaData: VisaBestallningMetadata
    ): VisaBestallningDto {

      val textMap = bestallningTexter.texter.map { it.id to it.value }.toMap()
      val invanareName = if (!invanareDto.sekretessMarkering) invanareDto.name else "Sekretessmarkerad uppgift"
      val isForfragan: (Fraga) -> Boolean = {
        metaData.scope == VisaBestallningScope.ALL || metaData.scope == VisaBestallningScope.FORFRAGAN
      }

      val isFaktureringsunderlag: (Fraga) -> Boolean = {
        metaData.scope == VisaBestallningScope.ALL || metaData.scope == VisaBestallningScope.FAKTURERINGSUNDERLAG
      }

      val forfraganFragor = toForfragan(bestallning, invanareName, textMap, metaData.bild, isForfragan)
      val faktureringsUnderlagFragor = toFaktureringsUnderlag(bestallning, textMap, isFaktureringsunderlag)

      val fragorList = ArrayList<Fraga>()

      fragorList.addAll(forfraganFragor)
      fragorList.addAll(faktureringsUnderlagFragor)

      return VisaBestallningDto(
         id = bestallning.id!!,
         status = bestallning.status!!.beskrivning,
         ankomstDatum = bestallning.ankomstDatum.toLocalDate(),
         intygTyp = bestallning.intygTyp,
         intygTypBeskrivning = bestallning.intygTypBeskrivning,
         invanare = invanareDto,
         metaData = metaData.metaData,
         fragor = fragorList
      )
    }

    fun toForfragan(
       bestallning: Bestallning,
       invanareName: String,
       textMap: Map<String, String>,
       bild: String,
       isForfragan: (Fraga) -> Boolean): List<Fraga> {

      return listOfNotNull(
         Fraga(
            rubrik = textMap.getValue(RBK_1),
            delfragor = listOf(
               Delfraga(etikett = textMap.getValue(ETK_1_1), bild = bild),
               Delfraga(etikett = textMap.getValue(ETK_1_2), text = textMap.getValue(TEXT_1_2_1)),
               Delfraga(etikett = textMap.getValue(ETK_1_3), text = textMap.getValue(TEXT_1_3_1)))
         ).takeIf(isForfragan),
         Fraga(
            rubrik = textMap.getValue(RBK_2),
            delfragor = listOf(
               Delfraga(etikett = textMap.getValue(ETK_2_1), svar = bestallning.invanare.personId.personnummerWithDash),
               Delfraga(etikett = textMap.getValue(ETK_2_2), svar = invanareName))
         ).takeIf(isForfragan),
         Fraga(
            rubrik = textMap.getValue(RBK_3),
            delfragor = listOf(
               Delfraga(etikett = textMap.getValue(ETK_3_1), svar = bestallning.syfte),
               Delfraga(etikett = textMap.getValue(ETK_3_2), svar = bestallning.invanare.bakgrundNulage),
               Delfraga(etikett = textMap.getValue(ETK_3_3), svar = bestallning.planeradeAktiviteter))
         ).takeIf(isForfragan),
         Fraga(
            rubrik = textMap.getValue(RBK_4),
            delfragor = listOf(
               Delfraga(etikett = textMap.getValue(ETK_4_1), svar = listOfNotNull(
                  bestallning.handlaggare.fullstandigtNamn,
                  bestallning.handlaggare.telefonnummer).joinToString(separator = "\n")),
               Delfraga(etikett = textMap.getValue(ETK_4_2), svar = listOfNotNull(
                  bestallning.handlaggare.kontor,
                  bestallning.handlaggare.adress).joinToString(separator = "\n"))
            )
         ).takeIf(isForfragan)
      )
    }

    fun toFaktureringsUnderlag(
       bestallning: Bestallning,
       textMap: Map<String, String>,
       isFaktureringsunderlag: (Fraga) -> Boolean): List<Fraga> {
      return listOfNotNull(
         Fraga(
            rubrik = textMap.getValue(RBK_5),
            delfragor = listOf(
               Delfraga(etikett = textMap.getValue(ETK_5_1), text = textMap.getValue(TEXT_5_1_1)),
               Delfraga(etikett = textMap.getValue(ETK_5_2), svar = bestallning.arendeReferens),
               Delfraga(etikett = textMap.getValue(ETK_5_3), svar = bestallning.handlaggare.kostnadsstalle),
               Delfraga(etikett = textMap.getValue(ETK_5_4), text = textMap.getValue(TEXT_5_4_1)),
               Delfraga(etikett = textMap.getValue(ETK_5_5), text = textMap.getValue(TEXT_5_5_1)))
         ).takeIf(isFaktureringsunderlag)
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

enum class BestallningMetadataTyp {
  MAIL_VIDAREBEFORDRA
}

data class BestallningMetaData(val typ: BestallningMetadataTyp, val text: String)


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
const val RBK_4: String = "RBK_4"
const val ETK_4_1: String = "ETK_4.1"
const val ETK_4_2: String = "ETK_4.2"

//Faktureringsunderlag
const val RBK_5: String = "RBK_5"
const val ETK_5_1: String = "ETK_5.1"
const val TEXT_5_1_1: String = "TEXT_5.1.1"
const val ETK_5_2: String = "ETK_5.2"
const val ETK_5_3: String = "ETK_5.3"
const val ETK_5_4: String = "ETK_5.4"
const val TEXT_5_4_1: String = "TEXT_5.4.1"
const val ETK_5_5: String = "ETK_5.5"
const val TEXT_5_5_1: String = "TEXT_5.5.1"
