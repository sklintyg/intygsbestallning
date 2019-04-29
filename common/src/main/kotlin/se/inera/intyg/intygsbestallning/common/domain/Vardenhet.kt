package se.inera.intyg.intygsbestallning.common.domain

data class Vardenhet(
   var hsaId: String,
   var vardgivareHsaId: String,
   val organisationId: String,
   var namn: String,
   var epost: String? = null
) {
  companion object Factory {
    fun newVardenhet(
       hsaId: String,
       vardgivareHsaId: String,
       organisationId: String,
       namn: String,
       epost: String?): Vardenhet {
      return Vardenhet(
         hsaId = hsaId,
         vardgivareHsaId = vardgivareHsaId,
         organisationId = organisationId,
         namn = namn,
         epost = epost)
    }
  }
}
