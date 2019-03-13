package se.inera.intyg.intygsbestallning.common.domain

import java.time.LocalDateTime

data class Notifiering(
   val id: Long? = null,
   val typ: NotifieringTyp,
   val mottagareHsaId: String,
   val skickad: LocalDateTime? = null
) {
  companion object Factory {
    fun inKommenBestallning(hsaId: String): Notifiering {
      return Notifiering(
         typ = NotifieringTyp.NY_BESTALLNING,
         mottagareHsaId = hsaId
      )
    }
  }
}
