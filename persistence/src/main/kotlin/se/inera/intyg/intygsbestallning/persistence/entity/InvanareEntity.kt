package se.inera.intyg.intygsbestallning.persistence.entity

import se.inera.intyg.intygsbestallning.common.domain.Invanare
import se.inera.intyg.schemas.contract.Personnummer
import javax.persistence.*

@Entity
@Table(name = "INVANARE")
class InvanareEntity private constructor(builder: Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  val id: Long?

  @Column(name = "PERSON_ID", nullable = false)
  val personId: String

  @Column(name = "BAKGRUND_NULAGE")
  var bakgrundNulage: String? = null

  init {
    this.id = builder.id
    this.personId = builder.personId ?: throw IllegalArgumentException("personId may not be null")
    this.bakgrundNulage = builder.bakgrundNulage
  }


  class Builder {
    var id: Long? = null
    var personId: String? = null
    var bakgrundNulage: String? = null

    fun id(id: Long?) = apply { this.id = id }
    fun personId(personId: String) = apply { this.personId = personId }
    fun bakgrundNulage(bakgrundNulage: String?) = apply { this.bakgrundNulage = bakgrundNulage }
    fun build() = InvanareEntity(this)
  }

  companion object Factory {

    fun toDomain(invanareEntity: InvanareEntity): Invanare {
      return Invanare(
         id = invanareEntity.id,
         personId = Personnummer.createPersonnummer(invanareEntity.personId).get(),
         bakgrundNulage = invanareEntity.bakgrundNulage)
    }

    fun toEntity(invanare: Invanare): InvanareEntity {
      return InvanareEntity.Builder()
         .id(invanare.id)
         .personId(invanare.personId.personnummerWithDash)
         .bakgrundNulage(invanare.bakgrundNulage)
         .build()
    }
  }
}

