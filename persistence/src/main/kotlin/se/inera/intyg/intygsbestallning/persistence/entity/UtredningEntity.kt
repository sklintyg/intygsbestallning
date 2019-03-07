package se.inera.intyg.intygsbestallning.persistence.entity

import se.inera.intyg.intygsbestallning.common.domain.Utredning
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "UTREDNING")
class UtredningEntity private constructor(builder: Builder) {

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
    this.bestallning = builder.bestallning ?: throw IllegalArgumentException("bestallning may not be null")
    this.vardenhet = builder.vardenhet ?: throw IllegalArgumentException("vardenhet may not be null")
  }

  class Builder {
    var id: Long? = null
    var bestallning: BestallningEntity? = null
    var vardenhet: VardenhetEntity? = null
    var avslutDatum: LocalDateTime? = null

    fun id(id: Long?) = apply { this.id = id }
    fun bestallning(bestallning: BestallningEntity) = apply { this.bestallning = bestallning }
    fun vardenhet(vardenhet: VardenhetEntity) = apply { this.vardenhet = vardenhet }
    fun avslutDatum(avslutDatum: LocalDateTime?) = apply { this.avslutDatum = avslutDatum }
    fun build() = UtredningEntity(this)
  }

  companion object Factory {

    fun toDomain(utredningEntity: UtredningEntity): Utredning {
      return Utredning(
         id = utredningEntity.id!!,
         bestallning = BestallningEntity.toDomain(utredningEntity.bestallning),
         vardenhet = VardenhetEntity.toDomain(utredningEntity.vardenhet),
         avslutDatum = utredningEntity.avslutDatum
      )
    }

    fun toEntity(utredning: Utredning): UtredningEntity {
      return UtredningEntity.Builder()
         .id(utredning.id)
         .bestallning(BestallningEntity.toEntity(utredning.bestallning))
         .vardenhet(VardenhetEntity.toEntity(utredning.vardenhet))
         .avslutDatum(utredning.avslutDatum)
         .build()
    }
  }
}
