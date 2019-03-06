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
package se.inera.intyg.intygsbestallning.persistence


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
class SkickadNotifieringEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private val id: Long? = null

    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYP", nullable = false)
    private val typ: NotifieringTyp? = null

    @Enumerated(value = EnumType.STRING)
    @Column(name = "MOTTAGARE", nullable = false)
    private val mottagare: NotifieringMottagarTyp? = null

    @Column(name = "MOTTAGARE_HSA_ID", nullable = false)
    private val mottagareHsaId: String? = null

    @Column(name = "ERSATTS", nullable = false, columnDefinition = "tinyint(1) default 0")
    private val ersatts: Boolean? = null

    @Column(name = "SKICKAD")
    //TODO: type below needed instead of java's LocalDateTime for mysql compatibillity?
    //@Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private val skickad: LocalDateTime? = null
}
