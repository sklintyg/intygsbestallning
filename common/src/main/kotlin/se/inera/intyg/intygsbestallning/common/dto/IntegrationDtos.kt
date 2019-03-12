package se.inera.intyg.intygsbestallning.common.dto

import se.inera.intyg.intygsbestallning.common.domain.IntygTyp

data class CreateBestallningRequest(
   val personnummer: String,
   val intygTyp: IntygTyp,
   val vardenhet: String
)

data class AccepteraBestallningRequest(
   val utredningId: String,
   val fritextForklaring: String? = null
)
