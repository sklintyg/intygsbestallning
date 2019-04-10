package se.inera.intyg.intygsbestallning.common.domain

import se.inera.intyg.schemas.contract.Personnummer

data class Invanare(
   val id: Long? = null,
   var personId: Personnummer,
   val bakgrundNulage: String? = null) {

  companion object Factory {
    fun newInvanare(
       personId: Personnummer,
       bakgrundNulage: String?) : Invanare {

      return Invanare(
         personId = personId,
         bakgrundNulage = bakgrundNulage
      )
    }
  }
}
