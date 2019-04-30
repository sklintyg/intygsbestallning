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

class IbJMSException : IbServiceException {
  constructor(errorCode: IbErrorCodeEnum, message: String) : super(errorCode, message)
  constructor(errorCode: IbErrorCodeEnum, message: String, errorEntityId: Long?) : super(errorCode, message, errorEntityId)
}

enum class NotFoundType(val errorText: String) {
  BESTALLNING("Felaktig bestallningsid: %s. Bestallningen existerar inte.")
}

enum class IbErrorCodeEnum {
  BAD_REQUEST,
  BAD_STATE,
  ALREADY_EXISTS,
  NOT_FOUND,
  UNKNOWN_INTERNAL_PROBLEM,
  EXTERNAL_ERROR,
  UNAUTHORIZED,
  PU_ERROR,
  LOGIN_FEL001,
  LOGIN_FEL002,
  LOGIN_FEL004
}

data class IbResponderValidationException(
   val errorCode: IbResponderValidationErrorCode,
   private val templateArguments: List<Any>) : RuntimeException() {

  override val message: String?
    get() {
      return MessageFormat.format(errorCode.errorMsgTemplate, templateArguments)
    }
}

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
