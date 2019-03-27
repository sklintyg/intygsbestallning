package se.inera.intyg.intygsbestallning.common.domain

enum class BestallningSvar(val code: String,
                           val codeSystem: String = "d9d51e92-e2c0-49d8-bbec-3fd7e1b60c85",
                           val codeSystemName: String = "KV svar beställning",
                           val klartext: String = "Svar på förfrågan och beställning.") {
    AVVISAT("AVVISAT"),
    ACCEPTERAT("ACCEPTERAT"),
    MAKULERAT("MAKULERAT"),
    RADERAT("RADERAT")
}