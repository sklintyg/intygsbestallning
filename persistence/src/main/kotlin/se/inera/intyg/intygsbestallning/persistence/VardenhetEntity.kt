package se.inera.intyg.intygsbestallning.persistence

import se.inera.intyg.intygsbestallning.common.Vardenhet
import javax.persistence.*

@Entity
@Table(name = "VARDENHET")
class VardenhetEntity private constructor(builder: VardenhetEntity.Builder) {

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
    var epost: String? = null
    var standardSvar: String? = null

    fun enhetNamn(enhetNamn: String) = apply { this.enhetNamn = enhetNamn }
    fun epost(epost: String?) = apply { this.epost = epost }
    fun standardSvar(standardSvar: String?) = apply { this.standardSvar = standardSvar }
    fun build() = VardenhetEntity(this)
  }
}

fun VardenhetEntity.toDomain(): Vardenhet {

  return Vardenhet(
     id = id!!,
     enhetNamn = enhetNamn,
     epost = epost,
     standardSvar = standardSvar)
}

fun Vardenhet.toEntity(): VardenhetEntity {
  return VardenhetEntity.Builder()
     .enhetNamn(this.enhetNamn)
     .epost(this.epost)
     .standardSvar(this.standardSvar)
     .build()
}
