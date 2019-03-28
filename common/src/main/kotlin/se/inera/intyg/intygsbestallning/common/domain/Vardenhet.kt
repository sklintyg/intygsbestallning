package se.inera.intyg.intygsbestallning.common.domain

data class Vardenhet(
   val id: Long? = null,
   var hsaId: String,
   var vardgivareHsaId: String,
   val organisationId: String,
   var namn: String,
   var epost: String? = null,
   var standardSvar: String? = null
) {
  companion object Factory {
    fun newVardenhet(
       hsaId: String,
       vardgivareHsaId: String,
       organisationId: String,
       namn: String,
       epost: String?,
       standardSvar: String?): Vardenhet {
      return Vardenhet(
         hsaId = hsaId,
         vardgivareHsaId = vardgivareHsaId,
         organisationId = organisationId,
         namn = namn,
         epost = epost,
         standardSvar = standardSvar)
    }
  }
}
