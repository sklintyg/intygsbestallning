package se.inera.intyg.intygsbestallning.persistence

import javax.persistence.*

@Entity
@Table(name = "UTREDNING")
class Utredning private constructor(builder: Utredning.Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID", nullable = false)
  val id: Long? = null

  @OneToOne(cascade = [CascadeType.ALL])
  @JoinColumn(name = "BESTALLNING_ID")
  val bestallning: Bestallning?

  @OneToOne(cascade = [CascadeType.ALL])
  @JoinColumn(name = "VARDENHET_ID")
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
