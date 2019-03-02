package se.inera.intyg.intygsbestallning.persistence

import java.lang.IllegalArgumentException
import javax.persistence.*

@Entity
@Table(name = "bestallning")
class Bestallning private constructor(builder: Bestallning.Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  val id: Long? = null

  @Column(name = "tilldelad_vardenhet_hsa_id", nullable = false)
  val vardenhetHsaId: String?

  @Column(name = "tilldelad_vardenhet_org_nr", nullable = false)
  val vardenehetOrgId: String?

  init {
    this.vardenhetHsaId = builder.vardenhetHsaId ?: throw IllegalArgumentException("vardenhetHsaId may not be null")
    this.vardenehetOrgId = builder.vardenehetOrgId ?: throw IllegalArgumentException("vardenehetOrgId may not be null")
  }

  class Builder {
    var vardenhetHsaId: String? = null
    var vardenehetOrgId: String? = null

    fun vardenhetHsaId(vardenhetHsaId: String) = apply { this.vardenhetHsaId = vardenhetHsaId }
    fun vardenehetOrgId(vardenehetOrgId: String) = apply { this.vardenehetOrgId = vardenehetOrgId }
    fun build() = Bestallning(this)
  }
}
