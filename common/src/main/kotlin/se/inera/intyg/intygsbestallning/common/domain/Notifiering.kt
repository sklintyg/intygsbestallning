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

data class Notifiering(
   val id: Long? = null,
   val typ: NotifieringTyp,
   val mottagareHsaId: String,
   var skickad: LocalDateTime? = null
) {
  companion object Factory {

    fun nyBestallning(hsaId: String): Notifiering {
      return Notifiering(
         typ = NotifieringTyp.NY_BESTALLNING,
         mottagareHsaId = hsaId
      )
    }

    fun paminnelse(hsaId: String, paminnelseTyp: NotifieringTyp): Notifiering {
      return Notifiering(
          typ = paminnelseTyp,
          mottagareHsaId = hsaId
      )
    }
  }
}
