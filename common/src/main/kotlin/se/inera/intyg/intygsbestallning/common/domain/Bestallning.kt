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
package se.inera.intyg.intygsbestallning.common.domain

import java.time.LocalDateTime

data class Bestallning(
   val id: Long? = null,
   val typ: String,
   val intygTyp: String,
   val intygTypBeskrivning: String,
   val ankomstDatum: LocalDateTime,
   val avslutDatum: LocalDateTime? = null,
   val syfte: String? = null,
   val planeradeAktiviteter: String? = null,
   var status: BestallningStatus? = BestallningStatus.UNDEFINED,
   val invanare: Invanare,
   val handlaggare: Handlaggare,
   val vardenhet: Vardenhet,
   val arendeReferens: String?,
   var handelser: List<Handelse>? = mutableListOf(),
   var notifieringar: List<Notifiering>? = mutableListOf()) {

  companion object Factory {

    @Suppress("LongParameterList")
    fun newBestallning(
       invanare: Invanare,
       syfte: String?,
       planeradeAktiviteter: String?,
       handlaggare: Handlaggare,
       typ: String,
       intygTyp: String,
       intygTypBeskrivning: String,
       vardenhet: Vardenhet,
       arendeReferens: String?): Bestallning {
      return Bestallning(
         typ = typ,
         intygTyp = intygTyp,
         intygTypBeskrivning = intygTypBeskrivning,
         ankomstDatum = LocalDateTime.now(),
         invanare = invanare,
         syfte = syfte,
         planeradeAktiviteter = planeradeAktiviteter,
         handlaggare = handlaggare,
         vardenhet = vardenhet,
         arendeReferens = arendeReferens,
         handelser = listOf(Handelse.skapa()),
         notifieringar = listOf(Notifiering.nyBestallning(vardenhet.hsaId)))
    }
  }
}
