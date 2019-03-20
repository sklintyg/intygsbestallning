package se.inera.intyg.intygsbestallning.common.domain

enum class BestallningStatus(val beskrivning: String) {
  UNDEFINED("undefined"),
  OLAST("Oläst"),
  LAST("Läst"),
  AVVISAD("Avvisad"),
  RADERAD("Raderad"),
  ACCEPTERAD("Accepterad"),
  KLAR("Klar")
}
