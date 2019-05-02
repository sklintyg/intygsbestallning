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
package se.inera.intyg.intygsbestallning.persistence.entity

import se.inera.intyg.intygsbestallning.common.domain.Invanare
import se.inera.intyg.schemas.contract.Personnummer
import javax.persistence.*

@Embeddable
class InvanareEntity private constructor(builder: Builder) {

  @Column(name = "PERSON_ID", nullable = false)
  val personId: String

  @Column(name = "BAKGRUND_NULAGE")
  var bakgrundNulage: String?

  init {
    this.personId = builder.personId ?: throw IllegalArgumentException("personId may not be null")
    this.bakgrundNulage = builder.bakgrundNulage
  }


  class Builder {
    var personId: String? = null
    var bakgrundNulage: String? = null

    fun personId(personId: String) = apply { this.personId = personId }
    fun bakgrundNulage(bakgrundNulage: String?) = apply { this.bakgrundNulage = bakgrundNulage }
    fun build() = InvanareEntity(this)
  }

  companion object Factory {

    fun toDomain(invanareEntity: InvanareEntity): Invanare {
      return Invanare(
         personId = Personnummer.createPersonnummer(invanareEntity.personId).get(),
         bakgrundNulage = invanareEntity.bakgrundNulage)
    }

    fun toEntity(invanare: Invanare): InvanareEntity {
      return InvanareEntity.Builder()
         .personId(invanare.personId.personnummerWithDash)
         .bakgrundNulage(invanare.bakgrundNulage)
         .build()
    }
  }
}

