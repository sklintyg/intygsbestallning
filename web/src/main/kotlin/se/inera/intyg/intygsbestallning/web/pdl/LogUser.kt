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
package se.inera.intyg.intygsbestallning.web.pdl

class LogUser  constructor(builder: LogUser.Builder) {

    val userId: String
    val userName: String?
    val userAssignment: String?
    val userTitle: String?
    val enhetsId: String
    val enhetsNamn: String?
    val vardgivareId: String
    val vardgivareNamn: String?

    init {
        this.userId = builder.userId
        this.userName = builder.userName
        this.userAssignment = builder.userAssignment
        this.userTitle = builder.userTitle
        this.enhetsId = builder.enhetsId
        this.enhetsNamn = builder.enhetsNamn
        this.vardgivareId = builder.vardgivareId
        this.vardgivareNamn = builder.vardgivareNamn
    }

    class Builder
    /**
     * Enligt tjänstekontraktsbeskrivningen ska det i anrop till tjänsten "StoreLog"
     * komma information om användaren som är upphov till loggposten. De fält som är
     * obligatoriska är användarens id, vårdenhetens id och vårdgivarens id.
     *
     * Se https://bitbucket.org/rivta-domains/riv.ehr.log/raw/master/docs/TKB_ehr_log.docx
     *
     * @param userId       HsaId of the logged in user.
     * @param enhetsId     HsaId of the enhet.
     * @param vardgivareId HsaId of the vardgivare.
     */
    (val userId: String,  val enhetsId: String,  val vardgivareId: String) {
         var userName: String? = null
         var userAssignment: String? = null
         var userTitle: String? = null
         var enhetsNamn: String? = null
         var vardgivareNamn: String? = null

        fun userName(userName: String) = apply { this.userName = userName }
        fun userAssignment(userAssignment: String) = apply { this.userAssignment = userAssignment }
        fun userTitle(userTitle: String) = apply { this.userTitle = userTitle }
        fun enhetsNamn(enhetsNamn: String) = apply { this.enhetsNamn = enhetsNamn }
        fun vardgivareNamn(vardgivareNamn: String) = apply { this.vardgivareNamn = vardgivareNamn }

        fun build() = LogUser(this)
    }
}
