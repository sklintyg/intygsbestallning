package se.inera.intyg.intygsbestallning.common.domain

data class Vardenhet(
   val id: Long? = null,
   val enhetNamn: String,
   val epost: String? = null,
   val standardSvar: String? = null
) {
  companion object Factory {
    fun newVardenhet(enhetNamn: String, epost: String?, standardSvar: String?): Vardenhet {
      return Vardenhet(enhetNamn = enhetNamn, epost = epost, standardSvar = standardSvar)
    }
  }
}
