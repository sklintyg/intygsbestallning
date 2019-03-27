package se.inera.intyg.intygsbestallning.common.text.bestallning

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

@JacksonXmlRootElement(localName = "bestallning")
class BestallningTexter {
  @JacksonXmlProperty(localName = "typ", isAttribute = true)
  val typ: String = ""

  @JacksonXmlProperty(localName = "version", isAttribute = true)
  val version: String = ""

  @JacksonXmlProperty(localName = "giltigFrom", isAttribute = true)
  val giltigFrom: String = ""

  @JacksonXmlProperty(localName = "bild", isAttribute = true)
  val bild: String = ""

  @JacksonXmlElementWrapper(localName = "texter")
  val texter: List<Text> = mutableListOf()
}

@JacksonXmlRootElement(localName = "text")
class Text {

  @JacksonXmlProperty(localName = "id", isAttribute = true)
  val id: String = ""

  @JacksonXmlText
  val value: String = ""
}
