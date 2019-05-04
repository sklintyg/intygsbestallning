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
package se.inera.intyg.intygsbestallning.common.text.bestallning

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

@JacksonXmlRootElement(localName = "bestallning")
class BestallningTexter {
  @JacksonXmlProperty(localName = "typ", isAttribute = true)
  val typ: String = ""

  @JacksonXmlProperty(localName = "giltigFrom", isAttribute = true)
  val giltigFrom: String = ""

  @JacksonXmlProperty(localName = "bild", isAttribute = true)
  val bild: String = ""

  @JacksonXmlProperty(localName = "paminnelse1", isAttribute = true)
  val paminnelse1: String = ""

  @JacksonXmlProperty(localName = "paminnelse2", isAttribute = true)
  val paminnelse2: String = ""

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
