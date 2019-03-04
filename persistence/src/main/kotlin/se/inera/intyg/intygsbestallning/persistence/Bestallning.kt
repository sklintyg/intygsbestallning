package se.inera.intyg.intygsbestallning.persistence

import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "bestallning")
class Bestallning private constructor(builder: Bestallning.Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  val id: Long? = null

  @Column(name = "ankomst_datum", nullable = false)
  val ankomstDatum: LocalDateTime

  init {
    this.ankomstDatum = builder.ankomstDatum ?: throw IllegalArgumentException("ankomstDatum may not be null")
  }

  class Builder {
    var ankomstDatum: LocalDateTime? = null

    fun ankomstDatum(ankomstDatum: LocalDateTime) = apply { this.ankomstDatum = ankomstDatum }
    fun build() = Bestallning(this)
  }
}
