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
package se.inera.intyg.intygsbestallning.common.text.mail

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "mail")
data class MailTexter(
   @JsonProperty(value = "logo") val logo: String,
   @JsonProperty(value = "intyg") val intyg: String,
   @JsonProperty(value = "typ") val typ: String,
   @JsonProperty(value = "arenderad") val arendeRad: ArendeRad,
   @JsonProperty(value = "halsning") val halsning: Halsning,
   @JsonProperty(value = "body") val body: Body,
   @JsonProperty(value = "footer") val footer: Footer)

data class ArendeRad(
   @JsonProperty(value = "arende") val arende: String
)

data class Halsning(
   @JsonProperty(value = "text") val text: String
)

data class Body(
   @JsonProperty(value = "text1") var text1: String,
   @JsonProperty(value = "text2") val text2: String
)

data class Footer(
   @JsonProperty(value = "text1") val text1: String,
   @JsonProperty(value = "text2") val text2: String
)
