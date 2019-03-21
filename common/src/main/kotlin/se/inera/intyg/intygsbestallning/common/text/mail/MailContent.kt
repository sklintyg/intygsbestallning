package se.inera.intyg.intygsbestallning.common.text.mail

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "mail")
data class MailContent(
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
   @JsonProperty(value = "text1") val text1: String,
   @JsonProperty(value = "text2") val text2: String
)

data class Footer(
   @JsonProperty(value = "text") val text: String
)
