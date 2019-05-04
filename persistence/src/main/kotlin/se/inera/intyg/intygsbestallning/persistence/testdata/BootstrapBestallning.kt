/**
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygsbestallning.persistence.testdata

import se.inera.intyg.intygsbestallning.common.domain.Bestallning
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus
import se.inera.intyg.intygsbestallning.common.domain.Handelse
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare
import se.inera.intyg.intygsbestallning.common.domain.Invanare
import se.inera.intyg.intygsbestallning.common.domain.Notifiering
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet
import java.time.LocalDateTime

data class BootstrapBestallning(
   val id: Long? = null,
   val intygTyp: String,
   var ankomstDatum: LocalDateTime,
   val avslutDatum: LocalDateTime? = null,
   val syfte: String? = null,
   val arendeReferens: String? = null,
   val planeradeAktiviteter: String? = null,
   var status: BestallningStatus? = BestallningStatus.UNDEFINED,
   var invanare: Invanare,
   val handlaggare: Handlaggare,
   var vardenhet: Vardenhet,
   var handelser: List<Handelse>? = mutableListOf(),
   var notifieringar: List<Notifiering>? = mutableListOf()
) {
  companion object Factory {
    fun toDomain(bootstrapBestallning: BootstrapBestallning): Bestallning {
      return Bestallning(
         id = bootstrapBestallning.id,
         intygTyp = bootstrapBestallning.intygTyp,
         ankomstDatum = bootstrapBestallning.ankomstDatum,
         avslutDatum = bootstrapBestallning.avslutDatum,
         syfte = bootstrapBestallning.syfte,
         planeradeAktiviteter = bootstrapBestallning.planeradeAktiviteter,
         status = bootstrapBestallning.status,
         arendeReferens = bootstrapBestallning.arendeReferens,
         invanare = bootstrapBestallning.invanare,
         handlaggare = bootstrapBestallning.handlaggare,
         vardenhet = bootstrapBestallning.vardenhet,
         handelser = bootstrapBestallning.handelser,
         notifieringar = bootstrapBestallning.notifieringar
      )
    }
  }
}
