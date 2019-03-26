package se.inera.intyg.intygsbestallning.common.dto

import se.inera.intyg.schemas.contract.Personnummer

data class CreateBestallningRequest(
   val personnummer: Personnummer,
   val fornamn: String? = null,
   val mellannamn: String? = null,
   val efternamn: String? = null,
   val bakgrundNulage: String? = null,
   val sektretessMarkering: Boolean? = false,
   val intygTyp: String,
   val intygVersion: Double,
   val vardenhet: String
)

data class AccepteraBestallningRequest(
   val bestallningId: String,
   val fritextForklaring: String? = null
)
