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
package se.inera.intyg.intygsbestallning.common.exception

import se.riv.intygsbestallning.certificate.order.v1.ErrorIdType
import java.text.MessageFormat
import java.util.*

open class IbServiceException : RuntimeException {
  val errorCode: IbErrorCodeEnum
  var errorEntityId: Long? = null
  var logId: String? = null

  constructor(errorCode: IbErrorCodeEnum, message: String) : super(message) {
    this.errorCode = errorCode
    this.errorEntityId = null
    this.logId = UUID.randomUUID().toString()
  }

  constructor(errorCode: IbErrorCodeEnum, message: String, errorEntityId: Long?) : super(message) {
    this.errorCode = errorCode
    this.errorEntityId = errorEntityId
    this.logId = UUID.randomUUID().toString()
  }
}

class IbJMSException(errorCode: IbErrorCodeEnum, message: String) : IbServiceException(errorCode, message)

enum class NotFoundType(val errorText: String) {
  BESTALLNING("Felaktig bestallningsid: %s. Bestallningen existerar inte.")
}

enum class IbErrorCodeEnum {
  BAD_REQUEST,
  BAD_STATE,
  ALREADY_EXISTS,
  EXTERNAL_ERROR,
  LOGIN_FEL001,
  LOGIN_FEL002,
  LOGIN_FEL004,
  GENERAL_FEL01_SPARA,
  GENERAL_FEL02_TEKNISKT,
  BESTALLNING_FEL001_RADERA,
  BESTALLNING_FEL002_UTSKRIFT,
  BESTALLNING_FEL004_TEKNISKT_FEL,
  BESTALLNING_FEL005_PU_ERROR,
  BESTALLNING_FEL008_ATKOMST_NEKAD,
  NOT_FOUND, //BE.FEL009
  VARDGIVARE_ORGNR_MISMATCH
}

data class IbResponderValidationException(
   val errorCode: IbResponderValidationErrorCode,
   private val templateArguments: List<Any>) : RuntimeException() {

  override val message: String?
    get() {
      return MessageFormat.format(errorCode.errorMsgTemplate, templateArguments)
    }
}

@Suppress("MaxLineLength")
enum class IbResponderValidationErrorCode(val errorMsgTemplate: String, val errorIdType: ErrorIdType) {
  GTA_FEL01("Unexpected codeSystem: {0}", ErrorIdType.VALIDATION_ERROR),
  GTA_FEL02("Unknown code: {0} for codeSystem: {1}", ErrorIdType.VALIDATION_ERROR),
  GTA_FEL03("Received technical error from HSA", ErrorIdType.TECHNICAL_ERROR),
  GTA_FEL04("Det uppstod ett fel när HSA Katalogen anropades. Beställningen kunde därför inte tas emot.", ErrorIdType.APPLICATION_ERROR),
  GTA_FEL05("{0} does not match expected format YYYYMMDDNNNN", ErrorIdType.VALIDATION_ERROR),
  GTA_FEL06("Vårdgivare saknar organisationsnummer", ErrorIdType.APPLICATION_ERROR),
  GTA_FEL07("Unexpected use of assessmentId", ErrorIdType.VALIDATION_ERROR),
  GTA_FEL08("Angiven intygstyp går inte att beställa", ErrorIdType.APPLICATION_ERROR),
  GTA_FEL09("Vårdenheten saknar e-postadress", ErrorIdType.EMAIL_ERROR),
  GTA_FEL10("Received technical error from PU", ErrorIdType.TECHNICAL_ERROR),
  GTA_FEL11("Angivet personnummer eller samordningsnummer finns inte i personuppgiftsregistret", ErrorIdType.APPLICATION_ERROR)
}
