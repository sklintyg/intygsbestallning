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

import com.querydsl.core.annotations.PropertyType
import com.querydsl.core.annotations.QueryType
import se.inera.intyg.intygsbestallning.common.domain.Bestallning
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Transient

@Entity
@Table(name = "BESTALLNING")
class BestallningEntity private constructor(builder: Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  val id: Long?

  @Column(name = "TYP", nullable = false)
  val typ: String

  @Column(name = "INTYG_TYP", nullable = false)
  val intygTyp: String

  @Column(name = "INTYG_TYP_BESKRIVNING", nullable = false)
  val intygTypBeskrivning: String

  @Column(name = "ANKOMST_DATUM", nullable = false)
  val ankomstDatum: LocalDateTime

  @Column(name = "ANKOMST_DATUM_STRING", nullable = false)
  val ankomstDatumString: String

  @Column(name = "AVSLUT_DATUM")
  var avslutDatum: LocalDateTime? = null

  @Column(name = "SYFTE")
  val syfte: String?

  @Column(name = "ARENDE_REFERENS")
  val arendeReferens: String?

  @Column(name = "PLANERADE_AKTIVITETER")
  val planeradeAktiviteter: String?

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false)
  val status: BestallningStatus

  @Column(name = "STATUS_STRING", nullable = false)
  val statusString: String

  @Embedded
  @AttributeOverrides(
          AttributeOverride(name = "personId", column = Column(name = "INVANARE_PERSON_ID")),
          AttributeOverride(name = "bakgrundNulage", column = Column(name = "INVANARE_BAKGRUND_NULAGE")))
  val invanare: InvanareEntity

  @OneToOne(cascade = [CascadeType.PERSIST])
  @JoinColumn(name = "HANDLAGGARE_ID", nullable = false)
  val handlaggare: HandlaggareEntity

  @Embedded
  @AttributeOverrides(
          AttributeOverride(name = "hsaId", column = Column(name = "VARDENHET_HSA_ID")),
          AttributeOverride(name = "vardgivareHsaId", column = Column(name = "VARDENHET_VARDGIVARE_HSA_ID")),
          AttributeOverride(name = "organisationId", column = Column(name = "VARDENHET_ORGANISATION_ID")),
          AttributeOverride(name = "namn", column = Column(name = "VARDENHET_NAMN")),
          AttributeOverride(name = "epost", column = Column(name = "VARDENHET_EPOST")))
  var vardenhet: VardenhetEntity

  @OneToMany(cascade = [CascadeType.ALL])
  @JoinColumn(name = "BESTALLNING_ID", referencedColumnName = "ID", nullable = false)
  val handelser: List<HandelseEntity>

  @OneToMany(cascade = [CascadeType.ALL])
  @JoinColumn(name = "BESTALLNING_ID", referencedColumnName = "ID", nullable = false)
  val notifieringar: List<NotifieringEntity>

  // to be able to make a text search
  @Transient
  @QueryType(PropertyType.STRING)
  var textSearch: String = ""

  init {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    this.id = builder.id
    this.typ = builder.typ ?: throw IllegalArgumentException("typ may not be null")
    this.intygTyp = builder.intygTyp ?: throw IllegalArgumentException("intygTyp may not be null")
    this.intygTypBeskrivning = builder.intygTypBeskrivning ?: throw IllegalArgumentException("intygTypBeskrivning may not be null")
    this.ankomstDatum = builder.ankomstDatum ?: throw IllegalArgumentException("ankomstDatum may not be null")
    this.ankomstDatumString = formatter.format(builder.ankomstDatum)
    this.avslutDatum = builder.avslutDatum
    this.syfte = builder.syfte
    this.arendeReferens = builder.arendeReferens
    this.planeradeAktiviteter = builder.planeradeAktiviteter
    this.status = builder.status ?: throw IllegalArgumentException("status may not be null")
    this.statusString = builder.status!!.beskrivning
    this.invanare = builder.invanare ?: throw IllegalArgumentException("invanare may not be null")
    this.handlaggare = builder.handlaggare ?: throw IllegalArgumentException("handlaggare may not be null")
    this.vardenhet = builder.vardenhet ?: throw IllegalArgumentException("vardenhet may not be null")
    this.handelser = builder.handelser
    this.notifieringar = builder.notifieringar
  }

  @Suppress("TooManyFunctions")
  class Builder {
    var id: Long? = null
    var typ: String? = null
    var intygTyp: String? = null
    var intygTypBeskrivning: String? = null
    var ankomstDatum: LocalDateTime? = null
    var avslutDatum: LocalDateTime? = null
    var syfte: String? = null
    var arendeReferens: String? = null
    var planeradeAktiviteter: String? = null
    var status: BestallningStatus? = null
    var invanare: InvanareEntity? = null
    var handlaggare: HandlaggareEntity? = null
    var vardenhet: VardenhetEntity? = null
    var handelser: List<HandelseEntity> = mutableListOf()
    var notifieringar: List<NotifieringEntity> = mutableListOf()

    fun id(id: Long?) = apply { this.id = id }
    fun typ(typ: String) = apply { this.typ = typ }
    fun intygTyp(intygTyp: String) = apply { this.intygTyp = intygTyp }
    fun intygTypBeskrivning(intygTypBeskrivning: String) = apply { this.intygTypBeskrivning = intygTypBeskrivning}
    fun ankomstDatum(ankomstDatum: LocalDateTime) = apply { this.ankomstDatum = ankomstDatum }
    fun avslutDatum(avslutDatum: LocalDateTime?) = apply { this.avslutDatum = avslutDatum }
    fun syfte(syfte: String?) = apply { this.syfte = syfte }
    fun arendeReferens(arendeReferens: String?) = apply { this.arendeReferens = arendeReferens }
    fun planeradeAktiviteter(planeradeAktiviteter: String?) = apply { this.planeradeAktiviteter = planeradeAktiviteter }
    fun status(status: BestallningStatus?) = apply { this.status = status }
    fun invanare(invanare: InvanareEntity?) = apply { this.invanare = invanare }
    fun handlaggare(handlaggare: HandlaggareEntity?) = apply { this.handlaggare = handlaggare }
    fun vardenhet(vardenhet: VardenhetEntity) = apply { this.vardenhet = vardenhet }
    fun handelser(handelser: List<HandelseEntity>) = apply { this.handelser = handelser }
    fun notifieringar(notifieringar: List<NotifieringEntity>) = apply { this.notifieringar = notifieringar }
    fun build() = BestallningEntity(this)
  }

  companion object Factory {
    fun toDomain(bestallningEntity: BestallningEntity): Bestallning {
      return Bestallning(
         id = bestallningEntity.id!!,
         typ = bestallningEntity.typ,
         intygTyp = bestallningEntity.intygTyp,
         intygTypBeskrivning = bestallningEntity.intygTypBeskrivning,
         ankomstDatum = bestallningEntity.ankomstDatum,
         avslutDatum = bestallningEntity.avslutDatum,
         syfte = bestallningEntity.syfte,
         arendeReferens = bestallningEntity.arendeReferens,
         planeradeAktiviteter = bestallningEntity.planeradeAktiviteter,
         status = bestallningEntity.status,
         invanare = InvanareEntity.toDomain(bestallningEntity.invanare),
         handlaggare = HandlaggareEntity.toDomain(bestallningEntity.handlaggare),
         vardenhet = VardenhetEntity.toDomain(bestallningEntity.vardenhet),
         handelser = bestallningEntity.handelser.map { HandelseEntity.toDomain(it) },
         notifieringar = bestallningEntity.notifieringar.map { NotifieringEntity.toDomain(it) })
    }

    fun toEntity(bestallning: Bestallning): BestallningEntity {
      return BestallningEntity.Builder()
         .id(bestallning.id)
         .typ(bestallning.typ)
         .intygTyp(bestallning.intygTyp)
         .intygTypBeskrivning(bestallning.intygTypBeskrivning)
         .ankomstDatum(bestallning.ankomstDatum)
         .syfte(bestallning.syfte)
         .avslutDatum(bestallning.avslutDatum)
         .status(bestallning.status)
         .arendeReferens(bestallning.arendeReferens)
         .planeradeAktiviteter(bestallning.planeradeAktiviteter)
         .invanare(InvanareEntity.toEntity(bestallning.invanare))
         .handlaggare(HandlaggareEntity.toEntity(bestallning.handlaggare))
         .vardenhet(VardenhetEntity.toEntity(bestallning.vardenhet))
         .handelser(bestallning.handelser!!.map { HandelseEntity.toEntity(it) })
         .notifieringar(bestallning.notifieringar!!.map { NotifieringEntity.toEntity(it) })
         .build()
    }
  }
}
