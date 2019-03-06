package se.inera.intyg.intygsbestallning.persistence.entity

import se.inera.intyg.intygsbestallning.common.Bestallning
import se.inera.intyg.intygsbestallning.common.BestallningStatus
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "BESTALLNING")
class BestallningEntity private constructor(builder: Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID", nullable = false)
  val id: Long? = null

  @Column(name = "ANKOMST_DATUM", nullable = false)
  val ankomstDatum: LocalDateTime

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false)
  val status: BestallningStatus

  @OneToMany(cascade = [CascadeType.ALL])
  @JoinColumn(name = "BESTALLNING_ID", referencedColumnName = "ID", nullable = false)
  val handelser: List<HandelseEntity>

  init {
    this.ankomstDatum = builder.ankomstDatum ?: throw IllegalArgumentException("ankomstDatum may not be null")
    this.status = builder.status ?: throw IllegalArgumentException("status may not be null")
    this.handelser = builder.handelser
  }

  class Builder {
    var ankomstDatum: LocalDateTime? = null
    var status: BestallningStatus? = null
    var handelser: List<HandelseEntity> = mutableListOf()

    fun ankomstDatum(ankomstDatum: LocalDateTime) = apply { this.ankomstDatum = ankomstDatum }
    fun status(status: BestallningStatus?) = apply { this.status = status }
    fun handelser(handelser: List<HandelseEntity>) = apply { this.handelser = handelser }
    fun build() = BestallningEntity(this)
  }

  companion object Factory {

    fun toDomain(bestallningEntity: BestallningEntity): Bestallning {
      return Bestallning(
         id = bestallningEntity.id!!,
         ankomstDatum = bestallningEntity.ankomstDatum,
         status = bestallningEntity.status,
         handelser = bestallningEntity.handelser.map { HandelseEntity.toDomain(it) })
    }

    fun toEntity(bestallning: Bestallning): BestallningEntity {
      return BestallningEntity.Builder()
         .status(bestallning.status)
         .ankomstDatum(bestallning.ankomstDatum)
         .handelser(bestallning.handelser!!.map { HandelseEntity.toEntity(it) })
         .build()
    }
  }
}
