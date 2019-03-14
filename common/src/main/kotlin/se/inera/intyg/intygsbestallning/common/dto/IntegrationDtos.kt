package se.inera.intyg.intygsbestallning.common.dto

import se.inera.intyg.intygsbestallning.common.domain.IntygTyp
import se.inera.intyg.schemas.contract.Personnummer

data class CreateBestallningRequest(
   val personnummer: Personnummer,
   val intygTyp: IntygTyp,
   val vardenhet: String
)

data class AccepteraBestallningRequest(
   val bestallningId: String,
   val fritextForklaring: String? = null
)
