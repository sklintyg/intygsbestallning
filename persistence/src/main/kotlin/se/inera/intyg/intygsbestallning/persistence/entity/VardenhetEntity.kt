package se.inera.intyg.intygsbestallning.persistence.entity

import se.inera.intyg.intygsbestallning.common.domain.Vardenhet
import javax.persistence.*

@Embeddable
class VardenhetEntity(builder: Builder) {

  @Column(name = "HSA_ID", nullable = false)
  val hsaId: String

  @Column(name = "VARDGIVARE_HSA_ID", nullable = false)
  val vardgivareHsaId: String

  @Column(name = "ORGANISATION_ID", nullable = false)
  val organisationId: String

  @Column(name = "NAMN", nullable = false)
  val namn: String

  @Column(name = "EPOST", nullable = false)
  val epost: String

  init {
    this.hsaId = builder.hsaId ?: throw IllegalArgumentException("hsaId may not be null")
    this.vardgivareHsaId = builder.vardgivareHsaId ?: throw IllegalArgumentException("vardgivareHsaId may not be null")
    this.organisationId = builder.organisationId ?: throw IllegalArgumentException("organisationId may not be null")
    this.namn = builder.namn ?: throw IllegalArgumentException("namn may not be null")
    this.epost = builder.epost?: throw IllegalArgumentException("epost may not be null")
  }

  class Builder {
    var hsaId: String? = null
    var vardgivareHsaId: String? = null
    var organisationId: String? = null
    var namn: String? = null
    var epost: String? = null

    fun hsaId(hsaId: String) = apply { this.hsaId = hsaId }
    fun vardgivareHsaId(vardgivareHsaId: String) = apply { this.vardgivareHsaId = vardgivareHsaId }
    fun organisationId(organisationId: String) = apply { this.organisationId = organisationId }
    fun namn(namn: String) = apply { this.namn = namn }
    fun epost(epost: String?) = apply { this.epost = epost }
    fun build() = VardenhetEntity(this)
  }

  companion object Factory {

    fun toDomain(vardenhetEntity: VardenhetEntity): Vardenhet {
      return Vardenhet(
         hsaId = vardenhetEntity.hsaId,
         vardgivareHsaId = vardenhetEntity.vardgivareHsaId,
         organisationId = vardenhetEntity.organisationId,
         namn = vardenhetEntity.namn,
         epost = vardenhetEntity.epost)
    }

    fun toEntity(vardenhet: Vardenhet): VardenhetEntity {
      return VardenhetEntity.Builder()
         .hsaId(vardenhet.hsaId)
         .vardgivareHsaId(vardenhet.vardgivareHsaId)
         .organisationId(vardenhet.organisationId)
         .namn(vardenhet.namn)
         .epost(vardenhet.epost)
         .build()
    }
  }
}

