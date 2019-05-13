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

import se.inera.intyg.intygsbestallning.common.domain.BestallningSvar
import se.inera.intyg.schemas.contract.Personnummer

data class CreateBestallningRequest(
   val invanare: CreateBestallningRequestInvanare,
   val handlaggare: CreateBestallningRequestHandlaggare,
   val syfte: String? = null,
   val planeradeInsatser: String? = null,
   val typ: String,
   val intygTyp: String,
   val intygTypBeskrivning: String,
   val vardenhet: String,
   val arendeReferens: String? = null
)

data class CreateBestallningRequestInvanare(
   val personnummer: Personnummer,
   val bakgrundNulage: String? = null
)

data class CreateBestallningRequestHandlaggare(
   val namn: String? = null,
   val telefonNummer: String? = null,
   val myndighet: String,
   val kontor: CreateBestallningRequestKontor
)

data class CreateBestallningRequestKontor(
   val namn: String,
   val kostnadsStalle: String? = null,
   val postAdress: String? = null,
   val postKod: String? = null,
   val postOrt: String? = null
)

abstract class SimpleBestallningRequest {
    abstract val userHsaId: String
    abstract val bestallningId: String
    abstract val hsaId: String
    abstract val orgNrVardgivare: String
    abstract val bestallningSvar: BestallningSvar
    abstract val fritextForklaring: String?
}


data class AccepteraBestallningRequest(
        override val userHsaId: String,
        override val bestallningId: String,
        override val hsaId: String,
        override val orgNrVardgivare: String,
        override val bestallningSvar: BestallningSvar = BestallningSvar.ACCEPTERAT,
        override val fritextForklaring: String? = null
) : SimpleBestallningRequest()

data class AvvisaBestallningRequest(
        override val userHsaId: String,
        override val bestallningId: String,
        override val hsaId: String,
        override val orgNrVardgivare: String,
        override val bestallningSvar: BestallningSvar = BestallningSvar.AVVISAT,
        override val fritextForklaring: String? = null
) : SimpleBestallningRequest()

data class RaderaBestallningRequest(
        override val userHsaId: String,
        override val bestallningId: String,
        override val hsaId: String,
        override val orgNrVardgivare: String,
        override val bestallningSvar: BestallningSvar = BestallningSvar.RADERAT,
        override val fritextForklaring: String? = null
) : SimpleBestallningRequest()

data class KlarmarkeraBestallningRequest(
    val userHsaId: String,
    val bestallningId: String,
    val hsaId: String,
    val orgNrVardgivare: String
)

data class VidareBefordraBestallningRequest(
   val bestallningId: String,
   val hsaId: String,
   val orgNrVardgivare: String
)

data class PdfBestallningRequest(
        val bestallningId: String,
        val hsaId: String,
        val orgNrVardgivare: String,
        val scope: VisaBestallningScope
)
