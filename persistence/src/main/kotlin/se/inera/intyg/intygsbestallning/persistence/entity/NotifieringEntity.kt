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


import se.inera.intyg.intygsbestallning.common.domain.Notifiering
import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "NOTIFIERING")
class NotifieringEntity private constructor(builder: Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private val id: Long?

  @Enumerated(value = EnumType.STRING)
  @Column(name = "TYP", nullable = false)
  private val typ: NotifieringTyp

  @Column(name = "MOTTAGARE_HSA_ID", nullable = false)
  private val mottagareHsaId: String

  @Column(name = "SKICKAD")
  private var skickad: LocalDateTime?

  init {
    this.id = builder.id
    this.typ = builder.typ ?: throw IllegalArgumentException("typ may not be null")
    this.mottagareHsaId = builder.mottagareHsaId ?: throw IllegalArgumentException("mottagareHsaId may not be null")
    this.skickad = builder.skickad
  }

  class Builder {
    var id: Long? = null
    var typ: NotifieringTyp? = null
    var mottagareHsaId: String? = null
    var skickad: LocalDateTime? = null

    fun id(id: Long?) = apply { this.id = id }
    fun typ(typ: NotifieringTyp?) = apply { this.typ = typ }
    fun mottagareHsaId(mottagareHsaId: String?) = apply { this.mottagareHsaId = mottagareHsaId }
    fun skickad(skickad: LocalDateTime?) = apply { this.skickad = skickad }
    fun build() = NotifieringEntity(this)
  }

  companion object Factory {

    fun toDomain(notifieringEntity: NotifieringEntity): Notifiering {
      return Notifiering(
         id = notifieringEntity.id,
         typ = notifieringEntity.typ,
         mottagareHsaId = notifieringEntity.mottagareHsaId,
         skickad = notifieringEntity.skickad)
    }

    fun toEntity(notifiering: Notifiering): NotifieringEntity {
      return NotifieringEntity.Builder()
         .id(notifiering.id)
         .typ(notifiering.typ)
         .mottagareHsaId(notifiering.mottagareHsaId)
         .skickad(notifiering.skickad)
         .build()
    }
  }
}
