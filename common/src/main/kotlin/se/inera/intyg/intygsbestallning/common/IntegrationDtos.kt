package se.inera.intyg.intygsbestallning.common

data class CreateUtredningRequest(
   val personnummer: String,
   val intygTyp: IntygTyp,
   val vardenhet: String
)

data class AccepteraBestallningRequest(
   val utredningId: String,
   val fritextForklaring: String? = null
)
