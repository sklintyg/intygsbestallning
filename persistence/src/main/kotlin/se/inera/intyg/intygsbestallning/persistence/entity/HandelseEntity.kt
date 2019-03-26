package se.inera.intyg.intygsbestallning.persistence.entity

import se.inera.intyg.intygsbestallning.common.domain.BestallningEvent
import se.inera.intyg.intygsbestallning.common.domain.Handelse
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "HANDELSE")
class HandelseEntity private constructor(builder: Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  val id: Long?

  @Enumerated(EnumType.STRING)
  @Column(name = "EVENT", nullable = false)
  val event: BestallningEvent

  @Column(name = "SKAPAD", nullable = false)
  val skapad: LocalDateTime

  @Column(name = "ANVANDARE")
  val anvandare: String?

  @Column(name = "BESKRIVNING", nullable = false)
  val beskrivning: String

  @Column(name = "KOMMENTAR")
  val kommentar: String?

  init {
    this.id = builder.id
    this.event = builder.event ?: throw IllegalArgumentException("event may not be null")
    this.skapad = builder.skapad ?: throw IllegalArgumentException("skapad may not be null")
    this.anvandare = builder.anvandare
    this.beskrivning = builder.beskrivning ?: throw IllegalArgumentException("beskrivning may not be null")
    this.kommentar = builder.kommentar
  }

  class Builder {
    var id: Long? = null
    var event: BestallningEvent? = null
    var skapad: LocalDateTime? = null
    var anvandare: String? = null
    var beskrivning: String? = null
    var kommentar: String? = null

    fun id(id: Long?) = apply { this.id = id }
    fun event(event: BestallningEvent?) = apply { this.event = event }
    fun skapad(skapad: LocalDateTime) = apply { this.skapad = skapad }
    fun anvandare(anvandare: String?) = apply { this.anvandare = anvandare }
    fun beskrivning(beskrivning: String?) = apply { this.beskrivning = beskrivning }
    fun kommentar(kommentar: String?) = apply { this.kommentar = kommentar }
    fun build() = HandelseEntity(this)
  }

  companion object Factory {

    fun toEntity(handelse: Handelse): HandelseEntity {
      return HandelseEntity.Builder()
         .id(handelse.id)
         .event(handelse.event)
         .skapad(handelse.skapad)
         .anvandare(handelse.anvandare)
         .beskrivning(handelse.beskrivning)
         .kommentar(handelse.kommentar)
         .build()
    }

    fun toDomain(handelseEntity: HandelseEntity): Handelse {
      return Handelse(
         id = handelseEntity.id,
         event = handelseEntity.event,
         skapad = handelseEntity.skapad,
         beskrivning = handelseEntity.beskrivning,
         kommentar = handelseEntity.kommentar)
    }
  }
}
