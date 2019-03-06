package se.inera.intyg.intygsbestallning.common

import java.time.LocalDateTime

data class Bestallning(
   val id: Long? = null,
   val ankomstDatum: LocalDateTime,
   var status: BestallningStatus? = BestallningStatus.UNDEFINED,
   var handelser: List<Handelse>? = mutableListOf()) {

  companion object Factory {
    fun newBestallning(): Bestallning {
      return Bestallning(
         ankomstDatum = LocalDateTime.now(),
         handelser = listOf(Handelse.skapa()))
    }
  }
}

enum class BestallningStatus {
  UNDEFINED,
  OLAST,
  LAST,
  AVVISAD,
  AVVISAD_RADERAD,
  ACCEPTERAD,
  KLARMARKERAD
}

enum class BestallningEvent {
  SKAPA,
  LAS,
  AVVISA,
  AVVISA_RADERA,
  ACCEPTERA,
  KLARMARKERA
}

data class Handelse(
   val id: Long? = null,
   val event: BestallningEvent,
   val skapad: LocalDateTime,
   val anvandare: String? = null,
   val beskrivning: String,
   val kommentar: String? = null
) {
  companion object Factory {
    fun skapa() : Handelse {
      return Handelse(event = BestallningEvent.SKAPA, skapad = LocalDateTime.now(), beskrivning = "Beställning mottagen")
    }
  }
}

data class Utredning(
   val id: Long? = null,
   val bestallning: Bestallning,
   val avslutDatum: LocalDateTime? = null,
   val vardenhet: Vardenhet
) {
  companion object Factory {
    fun newUtredning(bestallning: Bestallning, vardenhet: Vardenhet): Utredning {
      return Utredning(bestallning = bestallning, vardenhet = vardenhet)
    }
  }
}


data class Vardenhet(
   val id: Long? = null,
   val enhetNamn: String,
   val epost: String? = null,
   val standardSvar: String? = null
) {
  companion object Factory {
    fun newVardenhet(enhetNamn: String, epost: String?, standardSvar: String?): Vardenhet {
      return Vardenhet(enhetNamn = enhetNamn, epost = epost, standardSvar = standardSvar)
    }
  }
}

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

enum class IntygTyp(val kod: String, val klartext: String, val bestallningbar: Boolean) {
  LISJP("LISJP", "Läkarintyg sjukpenning", false),
  LUSE("LUSE", "Läkarutlåtande för sjukersättning", false),
  LUAE_NA("LUAE_NA", "Läkarutlåtande för aktivitetsersättning vid nedsatt arbetsförmåga", false),
  LUAE_FS("LUAE_FS", "Läkarutlåtande för aktivitetsersättning vid förlängd skolgång", false),
  DB("DB", "Dödsbevis", false),
  DOI("DOI", "Dödsorsaksintyg", false),
  LIAG("LIAG", "Läkarintyg till arbetsgivaren", false),
  AF00213("AF00213", "Arbetsförmedlingens medicinska utlåtande", true),
  TSTRK1007("TSTRK1007", "Läkarintyg- avseende högre körkortsbehörigheter eller taxiförarlegitimation- på begäran från Transportstyrelsen", false),
  TSTRK1031("TSTRK1031", "Läkarintyg diabetes avseende lämpligheten att inneha körkort m.m.", false),
  TSTRK1009("TSTRK1009", "Läkares anmälan om medicinsk olämplighet att inneha körkort, körkortstillstånd, traktorkort eller taxiförarlegitimation", false),
  TSTRK1062("TSTRK1062", "Läkarintyg avseende ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning", false),
  AG1_14("AG1-14", "Läkarintyg om arbetsförmåga – sjuklöneperiod", false),
  AG7804("AG7804", "Läkarintyg om arbetsförmåga – arbetsgivare", false),
  AFU("AFU", "Aktivitetsförmågeutredning", false),
  AFU_UTVIDGAD("AFU_UTVIDGAD", "Aktivitetsförmågeutredning utvidgad undersökning", false),
}























