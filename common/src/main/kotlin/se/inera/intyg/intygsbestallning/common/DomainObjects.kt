package se.inera.intyg.intygsbestallning.common

import java.time.LocalDateTime

data class Bestallning(
   val id: Long,
   val ankomstDatum: LocalDateTime,
   val status: BestallningStatus)

enum class BestallningStatus {
  OLAST,
  LAST,
  AVVISAD,
  AVVISAD_RADERAD
}

data class Utredning(
   val id: Long,
   val bestallning: Bestallning,
   val avslutDatum: LocalDateTime? = null,
   val vardenhet: Vardenhet
)

data class Vardenhet(
   val id: Long,
   val enhetNamn: String,
   val epost: String? = null,
   val standardSvar: String? = null
)

data class Invanare(
   val personnummer: String,
   val fornamn: String? = null,
   val mellannamn: String? = null,
   val efternamen: String? = null,
   val bakgrundNulage: String? = null,
   val sektretessMarkering: Boolean? = false)

data class Intyg(
   val id: Long,
   val intygTyp: IntygTyp,
   val invanare: Invanare
)

enum class IntygTyp(val kod: String, val klartext: String) {
  LISJP("LISJP", "Läkarintyg sjukpenning"),
  LUSE("LUSE", "Läkarutlåtande för sjukersättning"),
  LUAE_NA("LUAE_NA", "Läkarutlåtande för aktivitetsersättning vid nedsatt arbetsförmåga"),
  LUAE_FS("LUAE_FS", "Läkarutlåtande för aktivitetsersättning vid förlängd skolgång"),
  DB("DB", "Dödsbevis"),
  DOI("DOI", "Dödsorsaksintyg"),
  LIAG("LIAG", "Läkarintyg till arbetsgivaren"),
  AF00213("AF00213", "Arbetsförmedlingens medicinska utlåtande"),
  TSTRK1007("TSTRK1007", "Läkarintyg- avseende högre körkortsbehörigheter eller taxiförarlegitimation- på begäran från Transportstyrelsen"),
  TSTRK1031("TSTRK1031", "Läkarintyg diabetes avseende lämpligheten att inneha körkort m.m."),
  TSTRK1009("TSTRK1009", "Läkares anmälan om medicinsk olämplighet att inneha körkort, körkortstillstånd, traktorkort eller taxiförarlegitimation"),
  TSTRK1062("TSTRK1062", "Läkarintyg avseende ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning"),
  AG1_14("AG1-14", "Läkarintyg om arbetsförmåga – sjuklöneperiod"),
  AG7804("AG7804", "Läkarintyg om arbetsförmåga – arbetsgivare"),
  AFU("AFU", "Aktivitetsförmågeutredning"),
  AFU_UTVIDGAD("AFU_UTVIDGAD", "Aktivitetsförmågeutredning utvidgad undersökning")
}























