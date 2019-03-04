package se.inera.intyg.intygsbestallning.persistence

import javax.persistence.*

@Entity
@Table(name = "vardenhet")
class Vardenhet private constructor(builder: Vardenhet.Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  val id: Long? = null

  @Column(name = "enhet_namn", nullable = false)
  val enhetNamn: String

  @Column(name = "epost")
  val epost: String? = null

  @Column(name = "standard_svar")
  val standardSvar: String? = null

  init {
    this.enhetNamn = builder.enhetNamn ?: throw IllegalArgumentException("enhetNamn may not be null")
  }

  class Builder {
    var enhetNamn: String? = null

    fun enhetNamn(enhetNamn: String) = apply { this.enhetNamn = enhetNamn }
    fun build() = Vardenhet(this)
  }
}
