package se.inera.intyg.intygsbestallning.common.exception

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

class IbNotFoundException : IbServiceException {
  var type: NotFoundType? = null
  constructor(message: String) : super(IbErrorCodeEnum.NOT_FOUND, message)
  constructor(message: String, errorEntityId: Long?) : super(IbErrorCodeEnum.NOT_FOUND, message, errorEntityId)
  constructor(message: String, errorEntityId: Long?, type: NotFoundType) : super(IbErrorCodeEnum.NOT_FOUND, message, errorEntityId) {
    this.type= type
  }
}

enum class NotFoundType(val errorText: String) {
  UTREDNING("Felaktig utredningsid: %s. Utredningen existerar inte."),
  BESOK("Felaktig beöksid: %s. Besöket existerar inte.")
}

enum class IbErrorCodeEnum {
  BAD_REQUEST,
  BAD_STATE,
  ALREADY_EXISTS,
  NOT_FOUND,
  UNKNOWN_INTERNAL_PROBLEM,
  EXTERNAL_ERROR,
  UNAUTHORIZED
}
