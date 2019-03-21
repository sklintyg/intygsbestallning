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

import se.inera.intyg.infra.logmessages.ActivityPurpose
import se.inera.intyg.infra.logmessages.ActivityType
import se.inera.intyg.infra.logmessages.ResourceType
import java.time.LocalDateTime

data class LogMessage(val activity: LogActivity, val resources: List<LogResource>)

data class LogActivity(val activityLevel: String, val event: LogEvent) {
  val purpose: ActivityPurpose = ActivityPurpose.CARE_TREATMENT
  var startDate: LocalDateTime? = LocalDateTime.now()
}

data class LogResource(val patientId: String, val enhetsId: String, val vardgivareId: String) {
  val resourceType: ResourceType = ResourceType.RESOURCE_TYPE_BESTALLNING
  val patientNamn: String = "" // Patient name is not allowed to be displayed
  var enhetsNamn: String? = null
  var vardgivareNamn: String? = null
}

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
data class LogUser(val userId: String,  val enhetsId: String,  val vardgivareId: String) {
  var userName: String? = null
  var userAssignment: String? = null
  var userTitle: String? = null
  var enhetsNamn: String? = null
  var vardgivareNamn: String? = null
}

enum class LogEvent private constructor(val activityArgs: String, val activityType: ActivityType) {

  BESTALLNING_OPPNAS_OCH_LASES("Beställning läst", ActivityType.READ),
  BESTALLNING_ACCEPTERAS("Beställning accepterad", ActivityType.CREATE),
  BESTALLNING_AVVISAS("Beställning avvisad", ActivityType.UPDATE),
  BESTALLNING_KLARMARKERAS("Beställning klarmarkerad", ActivityType.UPDATE),
  BESTALLNING_SKRIV_UT("Beställning utskriven", ActivityType.PRINT),
  PERSONINFORMATION_VISAS_I_LISTA("Visad i lista", ActivityType.READ);

  val id: String

  init {
    this.id = this.name
  }
}
