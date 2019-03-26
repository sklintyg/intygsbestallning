package se.inera.intyg.intygsbestallning.persistence.entity

import com.querydsl.core.annotations.PropertyType
import com.querydsl.core.annotations.QueryType
import se.inera.intyg.intygsbestallning.common.domain.Bestallning
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "BESTALLNING")
class BestallningEntity private constructor(builder: Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  val id: Long?

  @Column(name = "INTYG_TYP", nullable = false)
  val intygTyp: String

  @Column(name = "INTYG_VERSION", nullable = false)
  val intygVersion: Double

  @Column(name = "ANKOMST_DATUM", nullable = false)
  val ankomstDatum: LocalDateTime

  @Column(name = "AVSLUT_DATUM")
  var avslutDatum: LocalDateTime? = null

  @Column(name = "SYFTE")
  val syfte: String?

  @Column(name = "PLANERADE_AKTIVITETER")
  val planeradeAktiviteter: String?

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false)
  val status: BestallningStatus

  @ManyToOne(cascade = [CascadeType.MERGE])
  @JoinColumn(name = "INVANARE_ID", nullable = false)
  val invanare: InvanareEntity

  @OneToOne(cascade = [CascadeType.PERSIST])
  @JoinColumn(name = "HANDLAGGARE_ID", nullable = false)
  val handlaggare: HandlaggareEntity

  @ManyToOne(cascade = [CascadeType.MERGE])
  @JoinColumn(name = "VARDENHET_ID", nullable = false)
  var vardenhet: VardenhetEntity

  @OneToMany(cascade = [CascadeType.ALL])
  @JoinColumn(name = "BESTALLNING_ID", referencedColumnName = "ID", nullable = false)
  val handelser: List<HandelseEntity>

  @OneToMany(cascade = [CascadeType.ALL])
  @JoinColumn(name = "BESTALLNING_ID", referencedColumnName = "ID", nullable = false)
  val notifieringar: List<NotifieringEntity>

  // to be able to make a text search
  @Transient
  @QueryType(PropertyType.STRING)
  var textSearch: String = ""

  init {
    this.id = builder.id
    this.intygTyp = builder.intygTyp ?: throw IllegalArgumentException("intygTyp may not be null")
    this.intygVersion = builder.intygVersion ?: throw IllegalArgumentException("intygVersion may not be null")
    this.ankomstDatum = builder.ankomstDatum ?: throw IllegalArgumentException("ankomstDatum may not be null")
    this.avslutDatum = builder.avslutDatum
    this.syfte = builder.syfte
    this.planeradeAktiviteter = builder.planeradeAktiviteter
    this.status = builder.status ?: throw IllegalArgumentException("status may not be null")
    this.invanare = builder.invanare ?: throw IllegalArgumentException("invanare may not be null")
    this.handlaggare = builder.handlaggare ?: throw IllegalArgumentException("handlaggare may not be null")
    this.vardenhet = builder.vardenhet ?: throw IllegalArgumentException("vardenhet may not be null")
    this.handelser = builder.handelser
    this.notifieringar = builder.notifieringar
  }

  class Builder {
    var id: Long? = null
    var intygTyp: String? = null
    var intygVersion: Double? = null
    var ankomstDatum: LocalDateTime? = null
    var avslutDatum: LocalDateTime? = null
    var syfte: String? = null
    var planeradeAktiviteter: String? = null
    var status: BestallningStatus? = null
    var invanare: InvanareEntity? = null
    var handlaggare: HandlaggareEntity? = null
    var vardenhet: VardenhetEntity? = null
    var handelser: List<HandelseEntity> = mutableListOf()
    var notifieringar: List<NotifieringEntity> = mutableListOf()

    fun id(id: Long?) = apply { this.id = id }
    fun intygTyp(intygTyp: String) = apply { this.intygTyp = intygTyp }
    fun intygVersion(intygVersion: Double) = apply { this.intygVersion = intygVersion }
    fun ankomstDatum(ankomstDatum: LocalDateTime) = apply { this.ankomstDatum = ankomstDatum }
    fun avslutDatum(avslutDatum: LocalDateTime?) = apply { this.avslutDatum = avslutDatum }
    fun syfte(syfte: String?) = apply { this.syfte = syfte }
    fun planeradeAktiviteter(planeradeAktiviteter: String?) = apply { this.planeradeAktiviteter = planeradeAktiviteter }
    fun status(status: BestallningStatus?) = apply { this.status = status }
    fun invanare(invanare: InvanareEntity?) = apply { this.invanare = invanare }
    fun handlaggare(handlaggare: HandlaggareEntity?) = apply { this.handlaggare = handlaggare }
    fun vardenhet(vardenhet: VardenhetEntity) = apply { this.vardenhet = vardenhet }
    fun handelser(handelser: List<HandelseEntity>) = apply { this.handelser = handelser }
    fun notifieringar(notifieringar: List<NotifieringEntity>) = apply { this.notifieringar = notifieringar }
    fun build() = BestallningEntity(this)
  }

  companion object Factory {
    fun toDomain(bestallningEntity: BestallningEntity): Bestallning {
      return Bestallning(
         id = bestallningEntity.id!!,
         intygTyp = bestallningEntity.intygTyp,
         intygVersion = bestallningEntity.intygVersion,
         ankomstDatum = bestallningEntity.ankomstDatum,
         avslutDatum = bestallningEntity.avslutDatum,
         syfte = bestallningEntity.syfte,
         planeradeAktiviteter = bestallningEntity.planeradeAktiviteter,
         status = bestallningEntity.status,
         invanare = InvanareEntity.toDomain(bestallningEntity.invanare),
         handlaggare = HandlaggareEntity.toDomain(bestallningEntity.handlaggare),
         vardenhet = VardenhetEntity.toDomain(bestallningEntity.vardenhet),
         handelser = bestallningEntity.handelser.map { HandelseEntity.toDomain(it) },
         notifieringar = bestallningEntity.notifieringar.map { NotifieringEntity.toDomain(it) })
    }

    fun toEntity(bestallning: Bestallning): BestallningEntity {
      return BestallningEntity.Builder()
         .id(bestallning.id)
         .intygTyp(bestallning.intygTyp)
         .intygVersion(bestallning.intygVersion)
         .ankomstDatum(bestallning.ankomstDatum)
         .avslutDatum(bestallning.avslutDatum)
         .status(bestallning.status)
         .invanare(InvanareEntity.toEntity(bestallning.invanare))
         .handlaggare(HandlaggareEntity.toEntity(bestallning.handlaggare))
         .vardenhet(VardenhetEntity.toEntity(bestallning.vardenhet))
         .handelser(bestallning.handelser!!.map { HandelseEntity.toEntity(it) })
         .notifieringar(bestallning.notifieringar!!.map { NotifieringEntity.toEntity(it) })
         .build()
    }


    fun toEntity(
       bestallning: Bestallning,
       invanareEntity: InvanareEntity,
       vardenhetEntity: VardenhetEntity): BestallningEntity {
      return BestallningEntity.Builder()
         .id(bestallning.id)
         .intygTyp(bestallning.intygTyp)
         .intygVersion(bestallning.intygVersion)
         .ankomstDatum(bestallning.ankomstDatum)
         .avslutDatum(bestallning.avslutDatum)
         .syfte(bestallning.syfte)
         .planeradeAktiviteter(bestallning.planeradeAktiviteter)
         .status(bestallning.status)
         .invanare(invanareEntity)
         .handlaggare(HandlaggareEntity.toEntity(bestallning.handlaggare))
         .vardenhet(vardenhetEntity)
         .handelser(bestallning.handelser!!.map { HandelseEntity.toEntity(it) })
         .notifieringar(bestallning.notifieringar!!.map { NotifieringEntity.toEntity(it) })
         .build()
    }
  }
}
