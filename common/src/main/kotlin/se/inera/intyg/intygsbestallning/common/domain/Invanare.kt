package se.inera.intyg.intygsbestallning.common.domain

data class Invanare(
   val personnummer: String,
   val fornamn: String? = null,
   val mellannamn: String? = null,
   val efternamen: String? = null,
   val bakgrundNulage: String? = null,
   val sektretessMarkering: Boolean? = false)
