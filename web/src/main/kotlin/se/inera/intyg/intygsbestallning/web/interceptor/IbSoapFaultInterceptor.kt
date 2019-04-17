package se.inera.intyg.intygsbestallning.web.interceptor

import org.apache.cxf.feature.transform.XSLTOutInterceptor
import org.apache.cxf.message.Message

class IbSoapFaultInterceptor(path: String) : XSLTOutInterceptor(path) {

  override fun handleMessage(message: Message) {
    message.exchange.outFaultMessage[Message.RESPONSE_CODE] = 200
    super.handleMessage(message)
  }
}
