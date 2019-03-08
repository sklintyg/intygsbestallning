/*
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
package se.inera.intyg.intygsbestallning.web.auth;

interface IbSelectableHsaEntity {
  val id: String
  val name: String

  fun type(): IbSelectableHsaEntityType
}

data class IbVardgivare(
    override val id: String,
    override val name: String,
    val isSamordnare: Boolean
) : IbSelectableHsaEntity {

  var vardenheter: ArrayList<IbVardenhet> = arrayListOf()

  override fun type() : IbSelectableHsaEntityType {
    return IbSelectableHsaEntityType.VG
  }
}

data class IbVardenhet(
    override val id: String,
    override val name: String,
    val vardgivareOrgnr: String,
    val parent: IbVardgivare
) : IbSelectableHsaEntity {

  override fun type() : IbSelectableHsaEntityType {
    return IbSelectableHsaEntityType.VE
  }
}

enum class IbSelectableHsaEntityType {
  VG, VE;
}
