package se.inera.intyg.intygsbestallning.common.domain

import se.inera.intyg.schemas.contract.Personnummer

data class Invanare(
   val id: Long? = null,
   val personId: Personnummer,
   val fornamn: String? = null,
   val mellannamn: String? = null,
   val efternamn: String? = null,
   val bakgrundNulage: String? = null,
   val sektretessMarkering: Boolean? = false) {

  companion object Factory {
    fun newInvanare(
       personId: Personnummer,
       fornamn: String?,
       mellannamn: String?,
       efternamn: String?,
       bakgrundNulage: String?,
       sektretessMarkering: Boolean?) : Invanare {

      return Invanare(
         personId = personId,
         fornamn = fornamn,
         mellannamn = mellannamn,
         efternamn = efternamn,
         bakgrundNulage = bakgrundNulage,
         sektretessMarkering = sektretessMarkering
      )
    }
  }
}
