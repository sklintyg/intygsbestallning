package se.inera.intyg.intygsbestallning.common.dto

import se.inera.intyg.schemas.contract.Personnummer

data class CreateBestallningRequest(
   val invanare: CreateBestallningRequestInvanare,
   val handlaggare: CreateBestallningRequestHandlaggare,
   val syfte: String? = null,
   val planeradeInsatser: String? = null,
   val intygTyp: String,
   val intygVersion: Double,
   val vardenhet: String
)

data class CreateBestallningRequestInvanare(
   val personnummer: Personnummer,
   val fornamn: String? = null,
   val mellannamn: String? = null,
   val efternamn: String? = null,
   val bakgrundNulage: String? = null
)

data class CreateBestallningRequestHandlaggare(
   val namn: String? = null,
   val telefonNummer: String,
   val email: String? = null,
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

data class AccepteraBestallningRequest(
   val bestallningId: String,
   val fritextForklaring: String? = null
)
