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

import se.inera.intyg.intygsbestallning.common.domain.Handlaggare
import javax.persistence.*

@Entity
@Table(name = "HANDLAGGARE")
class HandlaggareEntity private constructor(builder: Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private val id: Long?

  @Column(name = "FULLSTANDIGT_NAMN")
  private val fullstandigtNamn: String?

  @Column(name = "TELEFONNUMMER")
  private val telefonnummer: String?

  @Column(name = "EMAIL")
  private val email: String?

  @Column(name = "MYNDIGHET")
  private val myndighet: String

  @Column(name = "KONTOR")
  private val kontor: String?

  @Column(name = "KOSTNADSSTALLE")
  private val kostnadsstalle: String?

  @Column(name = "ADRESS")
  private val adress: String?

  @Column(name = "POSTNUMMER")
  private val postnummer: String?

  @Column(name = "STAD")
  private val stad: String?

  init {
    this.id = builder.id
    this.fullstandigtNamn = builder.fullstandigtNamn
    this.telefonnummer = builder.telefonnummer
    this.email = builder.email
    this.myndighet = builder.myndighet ?: throw IllegalArgumentException("myndighet may not be null")
    this.kontor = builder.kontor
    this.kostnadsstalle = builder.kostnadsstalle
    this.adress = builder.adress
    this.postnummer = builder.postnummer
    this.stad = builder.stad
  }

  class Builder {
    var id: Long? = null
    var fullstandigtNamn: String? = null
    var telefonnummer: String? = null
    var email: String? = null
    var myndighet: String? = null
    var kontor: String? = null
    var kostnadsstalle: String? = null
    var adress: String? = null
    var postnummer: String? = null
    var stad: String? = null

    fun id(id: Long?) = apply { this.id = id }
    fun fullstandigtNamn(fullstandigtNamn: String?) = apply { this.fullstandigtNamn = fullstandigtNamn }
    fun telefonnummer(telefonnummer: String?) = apply { this.telefonnummer = telefonnummer }
    fun email(email: String?) = apply { this.email = email }
    fun myndighet(myndighet: String?) = apply { this.myndighet = myndighet }
    fun kontor(kontor: String?) = apply { this.kontor = kontor }
    fun kostnadsstalle(kostnadsstalle: String?) = apply { this.kostnadsstalle = kostnadsstalle }
    fun adress(adress: String?) = apply { this.adress = adress }
    fun postnummer(postnummer: String?) = apply { this.postnummer = postnummer }
    fun stad(stad: String?) = apply { this.stad = stad }
    fun build() = HandlaggareEntity(this);
  }

  companion object Factory {

    fun toEntity(handlaggare: Handlaggare): HandlaggareEntity {
      return HandlaggareEntity.Builder()
         .id(handlaggare.id)
         .fullstandigtNamn(handlaggare.fullstandigtNamn)
         .telefonnummer(handlaggare.telefonnummer)
         .email(handlaggare.email)
         .myndighet(handlaggare.myndighet)
         .kontor(handlaggare.kontor)
         .kostnadsstalle(handlaggare.kostnadsstalle)
         .adress(handlaggare.adress)
         .postnummer(handlaggare.postnummer)
         .stad(handlaggare.stad)
         .build()
    }

    fun toDomain(handlaggareEntity: HandlaggareEntity): Handlaggare {
      return Handlaggare(
         id = handlaggareEntity.id,
         fullstandigtNamn = handlaggareEntity.fullstandigtNamn,
         telefonnummer = handlaggareEntity.telefonnummer,
         email = handlaggareEntity.email,
         myndighet = handlaggareEntity.myndighet,
         kontor = handlaggareEntity.kontor,
         kostnadsstalle = handlaggareEntity.kostnadsstalle,
         adress = handlaggareEntity.adress,
         postnummer = handlaggareEntity.postnummer,
         stad = handlaggareEntity.stad)
    }
  }
}
