package se.inera.intyg.intygsbestallning.common

import se.riv.intygsbestallning.certificate.order.respondtoorderresponder.v1.RespondToOrderType
import se.riv.intygsbestallning.certificate.order.v1.IIType

data class CreateUtredningRequest(
   val personnummer: String,
   val intygTyp: IntygTyp,
   val vardenhet: String
)

data class AccepteraBestallningRequest(
   val utredningId: String,
   val fritextForklaring: String? = null
)

fun AccepteraBestallningRequest.toRespondToOrderType(): RespondToOrderType {

  val assesmentId = IIType()
  assesmentId.extension = this.utredningId

  val to = RespondToOrderType()

  if (this.fritextForklaring != null) to.comment = this.fritextForklaring

  return to
}
