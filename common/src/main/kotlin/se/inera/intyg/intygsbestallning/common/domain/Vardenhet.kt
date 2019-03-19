package se.inera.intyg.intygsbestallning.common.domain

data class Vardenhet(
   val id: Long? = null,
   val hsaId: String,
   val vardgivareHsaId: String,
   val namn: String,
   val epost: String? = null,
   val standardSvar: String? = null
) {
  companion object Factory {
    fun newVardenhet(hsaId: String, vardgivareHsaId: String, namn: String, epost: String?, standardSvar: String?): Vardenhet {
      return Vardenhet(
         hsaId = hsaId,
         vardgivareHsaId = vardgivareHsaId,
         namn = namn,
         epost = epost,
         standardSvar = standardSvar)
    }
  }
}
