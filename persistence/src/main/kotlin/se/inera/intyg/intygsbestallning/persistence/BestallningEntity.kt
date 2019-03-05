package se.inera.intyg.intygsbestallning.persistence

import se.inera.intyg.intygsbestallning.common.Bestallning
import se.inera.intyg.intygsbestallning.common.BestallningStatus
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "BESTALLNING")
class BestallningEntity private constructor(builder: BestallningEntity.Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID", nullable = false)
  val id: Long? = null

  @Column(name = "ANKOMST_DATUM", nullable = false)
  val ankomstDatum: LocalDateTime

  @Column(name = "STATUS", nullable = false)
  val status: String

  init {
    this.ankomstDatum = builder.ankomstDatum ?: throw IllegalArgumentException("ankomstDatum may not be null")
    this.status = builder.status ?: throw IllegalArgumentException("status may not be null")
  }

  class Builder {
    var ankomstDatum: LocalDateTime? = null
    var status: String? = null

    fun ankomstDatum(ankomstDatum: LocalDateTime) = apply { this.ankomstDatum = ankomstDatum }
    fun status(status: String) = apply { this.status = status }
    fun build() = BestallningEntity(this)
  }
}

fun BestallningEntity.toDomain(): Bestallning {
  return Bestallning(
     id = this.id!!,
     ankomstDatum = ankomstDatum,
     status = BestallningStatus.valueOf(status))
}

fun Bestallning.toEntity(): BestallningEntity {
  return BestallningEntity.Builder()
     .status(this.status.toString())
     .ankomstDatum(this.ankomstDatum)
     .build()
}
