package se.inera.intyg.intygsbestallning.persistence

import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "BESTALLNING")
class Bestallning private constructor(builder: Bestallning.Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID", nullable = false)
  val id: Long? = null

  @Column(name = "ANKOMST_DATUM", nullable = false)
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
