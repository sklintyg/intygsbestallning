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

  init {
    this.bestallning = builder.bestallning
  }

  class Builder {
    var bestallning: Bestallning? = null

    fun bestallning(bestallning: Bestallning) = apply { this.bestallning = bestallning }
    fun build() = Utredning(this)
  }
}
