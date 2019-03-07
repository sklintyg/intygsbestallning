/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

import se.inera.intyg.intygsbestallning.common.domain.NotifieringPreference
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "NOTIFIERING_PREFERENCE")
class NotifieringPreferenceEntity private constructor(builder: Builder) {

    @Id
    @Column(name = "HSA_ID", nullable = false)
    var hsaId: String

    @Column(name = "LANDSTING_EPOST")
    var landstingEpost: String? = null

    init {
        this.hsaId = builder.hsaId ?: throw IllegalArgumentException("hsaId may not be null")
        this.landstingEpost = builder.landstingEpost
    }

    class Builder {
        var hsaId: String? = null
            private set
        var landstingEpost: String? = null
            private set

        fun hsaId(hsaId: String) = apply { this.hsaId = hsaId }
        fun landstingEpost(landstingEpost: String?) = apply { this.landstingEpost }
        fun build() = NotifieringPreferenceEntity(this)
    }

    companion object Factory {

        fun toDomain(npe: NotifieringPreferenceEntity): NotifieringPreference {
            return NotifieringPreference(
                    hsaId = npe.hsaId,
                    landstingEpost = npe.landstingEpost
            )
        }

        fun fromDomain(np: NotifieringPreference): NotifieringPreferenceEntity {
            return Builder()
                    .hsaId(np.hsaId)
                    .landstingEpost(np.landstingEpost)
                    .build()
        }
    }
}
