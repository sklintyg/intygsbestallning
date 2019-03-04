package se.inera.intyg.intygsbestallning.common

data class CreateUtredningRequest(
   val personnummer: String,
   val vardenhet: String
)
