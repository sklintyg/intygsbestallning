package se.inera.intyg.intygsbestallning.persistence

import se.inera.intyg.intygsbestallning.common.Utredning
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "UTREDNING")
class UtredningEntity private constructor(builder: UtredningEntity.Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID", nullable = false)
  var id: Long? = null

  @OneToOne(cascade = [CascadeType.ALL])
  @JoinColumn(name = "BESTALLNING_ID", nullable = false)
  val bestallning: BestallningEntity

  @OneToOne(cascade = [CascadeType.ALL])
  @JoinColumn(name = "VARDENHET_ID")
  val vardenhet: VardenhetEntity

  @Column(name = "AVSLUT_DATUM")
  val avslutDatum: LocalDateTime? = null

  init {
    this.bestallning = builder.bestallning ?: throw IllegalArgumentException("vardenhet may not be null")
    this.vardenhet = builder.vardenhet ?: throw IllegalArgumentException("vardenhet may not be null")
  }

  class Builder {
    var bestallning: BestallningEntity? = null
    var vardenhet: VardenhetEntity? = null
    var avslutDatum: LocalDateTime? = null

    fun bestallning(bestallning: BestallningEntity) = apply { this.bestallning = bestallning }
    fun vardenhet(vardenhet: VardenhetEntity) = apply { this.vardenhet = vardenhet }
    fun avslutDatum(avslutDatum: LocalDateTime) = apply { this.avslutDatum = avslutDatum }
    fun build() = UtredningEntity(this)
  }
}

fun UtredningEntity.toDomain(): se.inera.intyg.intygsbestallning.common.Utredning {
  return se.inera.intyg.intygsbestallning.common.Utredning(
     id = this.id!!,
     bestallning = bestallning.toDomain(),
     vardenhet = vardenhet.toDomain(),
     avslutDatum = avslutDatum
  )
}

fun Utredning.toEntity(): se.inera.intyg.intygsbestallning.persistence.UtredningEntity {
  return UtredningEntity.Builder()
     .bestallning(this.bestallning.toEntity())
     .vardenhet(this.vardenhet.toEntity())
     .build()
}
