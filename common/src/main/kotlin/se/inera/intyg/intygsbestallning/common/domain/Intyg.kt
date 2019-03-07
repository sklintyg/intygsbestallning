package se.inera.intyg.intygsbestallning.common.domain

data class Intyg(
   val id: Long,
   val intygTyp: IntygTyp,
   val invanare: Invanare
)
