package se.inera.intyg.intygsbestallning.persistence

import javax.persistence.*

@Entity
@Table(name = "VARDENHET")
class Vardenhet private constructor(builder: Vardenhet.Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID", nullable = false)
  val id: Long? = null

  @Column(name = "ENHET_NAMN", nullable = false)
  val enhetNamn: String

  @Column(name = "EPOST")
  val epost: String? = null

  @Column(name = "STANDARD_SVAR")
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
