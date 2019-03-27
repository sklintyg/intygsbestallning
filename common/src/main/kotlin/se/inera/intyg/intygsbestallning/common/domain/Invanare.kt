package se.inera.intyg.intygsbestallning.common.domain

import se.inera.intyg.schemas.contract.Personnummer

data class Invanare(
   val id: Long? = null,
   var personId: Personnummer,
   var fornamn: String? = null,
   var mellannamn: String? = null,
   var efternamn: String? = null,
   val bakgrundNulage: String? = null,
   var sektretessMarkering: Boolean? = false) {

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
