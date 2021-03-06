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
package se.inera.intyg.intygsbestallning.web.auth

import java.io.Serializable

interface IbSelectableHsaEntity : Serializable {
  val id: String
  val name: String

  fun type(): IbSelectableHsaEntityType
}

data class IbVardgivare(
    override val id: String,
    override val name: String
) : IbSelectableHsaEntity {
  var vardenheter: ArrayList<IbVardenhet> = arrayListOf()

  companion object {
    private const val serialVersionUID: Long = -6427803280385145071L
  }

  override fun type() : IbSelectableHsaEntityType {
    return IbSelectableHsaEntityType.VG
  }
}

data class IbVardenhet(
    override val id: String,
    override val name: String,
    val parentHsaId: String,
    val parentHsaName: String,
    val orgNrVardgivare: String
) : IbSelectableHsaEntity {

  companion object {
    private const val serialVersionUID: Long = -5646195952617853831L
  }

  override fun type() : IbSelectableHsaEntityType {
    return IbSelectableHsaEntityType.VE
  }
}

enum class IbSelectableHsaEntityType {
  VG, VE;
}
