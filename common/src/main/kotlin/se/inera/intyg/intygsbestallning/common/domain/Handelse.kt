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

data class Handelse(
   val id: Long? = null,
   val event: BestallningEvent,
   val skapad: LocalDateTime,
   val anvandare: String? = null,
   val beskrivning: String,
   val kommentar: String? = null
) {
  companion object Factory {
    fun skapa(): Handelse {
      return Handelse(
         event = BestallningEvent.SKAPA, skapad = LocalDateTime.now(), beskrivning = "Beställning mottagen")
    }

    fun las(): Handelse {
      return Handelse(
         event = BestallningEvent.LAS, skapad = LocalDateTime.now(), beskrivning = "Beställning läst")
    }

    fun acceptera(): Handelse {
      return Handelse(
         event = BestallningEvent.ACCEPTERA, skapad = LocalDateTime.now(), beskrivning = "Beställning accepterad")
    }

    fun avvisa(): Handelse {
      return Handelse(
         event = BestallningEvent.AVVISA, skapad = LocalDateTime.now(), beskrivning = "Beställning avvisad")
    }

    fun klarmarkera(): Handelse {
      return Handelse(
       event = BestallningEvent.KLARMARKERA, skapad = LocalDateTime.now(), beskrivning = "Beställning klarmarkerad")
    }
  }
}
