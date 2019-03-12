package se.inera.intyg.intygsbestallning.persistence.entity

import se.inera.intyg.intygsbestallning.common.domain.Bestallning
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus
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

  @Column(name = "AVSLUT_DATUM")
  var avslutDatum: LocalDateTime? = null

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false)
  val status: BestallningStatus

  @OneToOne(cascade = [CascadeType.ALL])
  @JoinColumn(name = "VARDENHET_ID")
  val vardenhet: VardenhetEntity

  @OneToMany(cascade = [CascadeType.ALL])
  @JoinColumn(name = "BESTALLNING_ID", referencedColumnName = "ID", nullable = false)
  val handelser: List<HandelseEntity>

  @OneToMany(cascade = [CascadeType.ALL])
  @JoinColumn(name = "BESTALLNING_ID", referencedColumnName = "ID", nullable = false)
  val notifieringar: List<NotifieringEntity>

  init {
    this.ankomstDatum = builder.ankomstDatum ?: throw IllegalArgumentException("ankomstDatum may not be null")
    this.avslutDatum = builder.avslutDatum ?: throw IllegalArgumentException("avslutDatum may not be null")
    this.status = builder.status ?: throw IllegalArgumentException("status may not be null")
    this.vardenhet = builder.vardenhet ?: throw IllegalArgumentException("vardenhet may not be null")
    this.handelser = builder.handelser
    this.notifieringar = builder.notifieringar
  }

  class Builder {
    var ankomstDatum: LocalDateTime? = null
    var avslutDatum: LocalDateTime? = null
    var status: BestallningStatus? = null
    var vardenhet: VardenhetEntity? = null
    var handelser: List<HandelseEntity> = mutableListOf()
    var notifieringar: List<NotifieringEntity> = mutableListOf()

    fun ankomstDatum(ankomstDatum: LocalDateTime) = apply { this.ankomstDatum = ankomstDatum }
    fun avslutDatum(avslutDatum: LocalDateTime?) = apply { this.avslutDatum = avslutDatum }
    fun status(status: BestallningStatus?) = apply { this.status = status }
    fun vardenhet(vardenhet: VardenhetEntity) = apply { this.vardenhet = vardenhet }
    fun handelser(handelser: List<HandelseEntity>) = apply { this.handelser = handelser }
    fun notifieringar(notifieringar: List<NotifieringEntity>) = apply { this.notifieringar = notifieringar }
    fun build() = BestallningEntity(this)
  }

  companion object Factory {

    fun toDomain(bestallningEntity: BestallningEntity): Bestallning {
      return Bestallning(
         id = bestallningEntity.id!!,
         ankomstDatum = bestallningEntity.ankomstDatum,
         avslutDatum = bestallningEntity.avslutDatum,
         status = bestallningEntity.status,
         vardenhet = VardenhetEntity.toDomain(bestallningEntity.vardenhet),
         handelser = bestallningEntity.handelser.map { HandelseEntity.toDomain(it) })
    }

    fun toEntity(bestallning: Bestallning): BestallningEntity {
      return BestallningEntity.Builder()
         .status(bestallning.status)
         .ankomstDatum(bestallning.ankomstDatum)
         .avslutDatum(bestallning.avslutDatum)
         .handelser(bestallning.handelser!!.map { HandelseEntity.toEntity(it) })
         .notifieringar(bestallning.notifieringar!!.map { NotifieringEntity.toEntity(it) })
         .build()
    }
  }
}
