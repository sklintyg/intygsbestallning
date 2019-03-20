package se.inera.intyg.intygsbestallning.persistence.entity

import se.inera.intyg.intygsbestallning.common.domain.Vardenhet
import javax.persistence.*

@Entity
@Table(name = "VARDENHET")
class VardenhetEntity (builder: Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  val id: Long?

  @Column(name = "HSA_ID", nullable = false)
  val hsaId: String

  @Column(name = "VARDGIVARE_HSA_ID", nullable = false)
  val vardgivareHsaId: String

  @Column(name = "NAMN", nullable = false)
  val namn: String

  @Column(name = "EPOST")
  val epost: String? = null

  @Column(name = "STANDARD_SVAR")
  val standardSvar: String? = null

  init {
    this.id = builder.id
    this.hsaId = builder.hsaId ?: throw IllegalArgumentException("hsaId may not be null")
    this.vardgivareHsaId = builder.vardgivareHsaId ?: throw IllegalArgumentException("vardgivareHsaId may not be null")
    this.namn = builder.namn ?: throw IllegalArgumentException("namn may not be null")
  }

  class Builder {
    var id: Long? = null
    var hsaId: String? = null
    var vardgivareHsaId: String? = null
    var namn: String? = null
    var epost: String? = null
    var standardSvar: String? = null

    fun id(id: Long?) = apply { this.id = id }
    fun hsaId(hsaId: String) = apply { this.hsaId = hsaId }
    fun vardgivareHsaId(vardgivareHsaId: String) = apply { this.vardgivareHsaId = vardgivareHsaId }
    fun namn(namn: String) = apply { this.namn = namn }
    fun epost(epost: String?) = apply { this.epost = epost }
    fun standardSvar(standardSvar: String?) = apply { this.standardSvar = standardSvar }
    fun build() = VardenhetEntity(this)
  }

  companion object Factory {

    fun toDomain(vardenhetEntity: VardenhetEntity): Vardenhet {
      return Vardenhet(
         id = vardenhetEntity.id!!,
         hsaId = vardenhetEntity.hsaId,
         vardgivareHsaId = vardenhetEntity.vardgivareHsaId,
         namn = vardenhetEntity.namn,
         epost = vardenhetEntity.epost,
         standardSvar = vardenhetEntity.standardSvar)
    }

    fun toEntity(vardenhet: Vardenhet): VardenhetEntity {
      return VardenhetEntity.Builder()
         .id(vardenhet.id)
         .hsaId(vardenhet.hsaId)
         .vardgivareHsaId(vardenhet.vardgivareHsaId)
         .namn(vardenhet.namn)
         .epost(vardenhet.epost)
         .standardSvar(vardenhet.standardSvar)
         .build()
    }
  }
}

