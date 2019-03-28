package se.inera.intyg.intygsbestallning.persistence.testdata

import com.fasterxml.jackson.annotation.JacksonInject
import se.inera.intyg.intygsbestallning.common.domain.*
import java.time.LocalDateTime

data class BootstrapBestallning(
   val id: Long? = null,
   val intygTyp: String,
   val intygVersion: Double,
   var ankomstDatum: LocalDateTime,
   val avslutDatum: LocalDateTime? = null,
   val syfte: String? = null,
   val planeradeAktiviteter: String? = null,
   var status: BestallningStatus? = BestallningStatus.UNDEFINED,
   val invanare: Invanare,
   val handlaggare: Handlaggare,
   val vardenhet: Vardenhet,
   var handelser: List<Handelse>? = mutableListOf(),
   var notifieringar: List<Notifiering>? = mutableListOf()
) {
  companion object Factory {
    fun toDomain(bootstrapBestallning: BootstrapBestallning): Bestallning {
      return Bestallning(
         id = bootstrapBestallning.id,
         intygTyp = bootstrapBestallning.intygTyp,
         intygVersion = bootstrapBestallning.intygVersion,
         ankomstDatum = bootstrapBestallning.ankomstDatum,
         avslutDatum = bootstrapBestallning.avslutDatum,
         syfte = bootstrapBestallning.syfte,
         planeradeAktiviteter = bootstrapBestallning.planeradeAktiviteter,
         status = bootstrapBestallning.status,
         invanare = bootstrapBestallning.invanare,
         handlaggare = bootstrapBestallning.handlaggare,
         vardenhet = bootstrapBestallning.vardenhet,
         handelser = bootstrapBestallning.handelser,
         notifieringar = bootstrapBestallning.notifieringar
      )
    }
  }
}
