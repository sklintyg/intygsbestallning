package se.inera.intyg.intygsbestallning.persistence

import javax.persistence.*

@Entity
@Table(name = "utredning")
class Utredning private constructor(builder: Utredning.Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  val id: Long? = null

  @OneToOne(cascade = [CascadeType.ALL])
  @JoinColumn(name = "bestallning_id")
  val bestallning: Bestallning?

  @OneToOne(cascade = [CascadeType.ALL])
  @JoinColumn(name = "vardenhet_id")
  val vardenhet: Vardenhet

  init {
    this.bestallning = builder.bestallning ?: throw IllegalArgumentException("vardenhet may not be null")
    this.vardenhet = builder.vardenhet ?: throw IllegalArgumentException("vardenhet may not be null")
  }

  class Builder {
    var bestallning: Bestallning? = null
    var vardenhet: Vardenhet? = null

    fun bestallning(bestallning: Bestallning) = apply { this.bestallning = bestallning }
    fun vardenhet(vardenhet: Vardenhet) = apply { this.vardenhet = vardenhet }
    fun build() = Utredning(this)
  }
}
