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

import se.inera.intyg.schemas.contract.Personnummer

data class Invanare(
   val id: Long? = null,
   var personId: Personnummer,
   var bakgrundNulage: String? = null) {

  companion object Factory {
    fun newInvanare(
       personId: Personnummer,
       bakgrundNulage: String?) : Invanare {

      return Invanare(
         personId = personId,
         bakgrundNulage = bakgrundNulage
      )
    }
  }
}
