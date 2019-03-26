package se.inera.intyg.intygsbestallning.common.domain

data class Handlaggare(
   val id: Long? = null,
   val fullstandigtNamn: String? = null,
   val telefonnummer: String? = null,
   val email: String? = null,
   val myndighet: String? = null,
   val kontor: String? = null,
   val kostnadsstalle: String? = null,
   val adress: String? = null,
   val postnummer: String? = null,
   val stad: String? = null
) {
  companion object Factory {
    fun newHandlaggare(
       fullstandigtNam: String?,
       telefonnummer: String?,
       email: String?,
       myndighet: String,
       kontor: String?,
       kostnadsstalle: String?,
       adress: String?,
       postnummer: String?,
       stad: String?): Handlaggare {
      return Handlaggare(
         fullstandigtNamn = fullstandigtNam,
         telefonnummer = telefonnummer,
         email = email,
         myndighet = myndighet,
         kontor = kontor,
         kostnadsstalle = kostnadsstalle,
         adress = adress,
         postnummer = postnummer,
         stad = stad
      )
    }
  }
}
