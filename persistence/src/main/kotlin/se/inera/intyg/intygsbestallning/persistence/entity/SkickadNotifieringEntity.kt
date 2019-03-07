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
package se.inera.intyg.intygsbestallning.persistence.entity


import se.inera.intyg.intygsbestallning.persistence.part.NotifieringMottagarTyp
import se.inera.intyg.intygsbestallning.persistence.part.NotifieringTyp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "SKICKAD_NOTIFIERING")
class SkickadNotifieringEntity private constructor(builder: Builder) {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private val id: Long?

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYP", nullable = false)
    private val typ: NotifieringTyp?

    @Enumerated(value = EnumType.STRING)
    @Column(name = "MOTTAGARE", nullable = false)
    private val mottagare: NotifieringMottagarTyp?

    @Column(name = "MOTTAGARE_HSA_ID", nullable = false)
    private val mottagareHsaId: String?

    @Column(name = "ERSATTS", nullable = false, columnDefinition = "tinyint(1) default 0")
    private val ersatts: Boolean?

    @Column(name = "SKICKAD")
    private val skickad: LocalDateTime?

    init {
        this.id = builder.id
        this.typ = builder.typ
        this.mottagare = builder.mottagare
        this.mottagareHsaId = builder.mottagareHsaId
        this.ersatts = builder.ersatts
        this.skickad = builder.skickad
    }

    class Builder {
        var id: Long? = null
            private set
        var typ: NotifieringTyp? = null
            private set
        var mottagare: NotifieringMottagarTyp? = null
            private set
        var mottagareHsaId: String? = null
            private set
        var ersatts: Boolean? = null
            private set
        var skickad: LocalDateTime? = null
            private set

        fun id(id: Long?) = apply { this.id = id }
        fun typ(typ: NotifieringTyp?) = apply { this.typ = typ }
        fun mottagare(mottagare: NotifieringMottagarTyp?) = apply { this.mottagare = mottagare }
        fun mottagareHsaId(mottagareHsaId: String?) = apply { this.mottagareHsaId = mottagareHsaId }
        fun ersatts(ersatts: Boolean?) = apply { this.ersatts = ersatts }
        fun skickad(skickad: LocalDateTime?) = apply { this.skickad = skickad }
        fun build() = SkickadNotifieringEntity(this)
    }
}
