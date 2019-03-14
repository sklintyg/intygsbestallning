package se.inera.intyg.intygsbestallning.persistence.entity

import se.inera.intyg.intygsbestallning.common.domain.Invanare
import se.inera.intyg.schemas.contract.Personnummer
import javax.persistence.*

@Entity
@Table(name = "INVANARE")
class InvanareEntity private constructor(builder: Builder) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID", nullable = false)
  val id: Long? = null

  @Column(name = "PERSON_ID", nullable = false)
  val personId: String

  @Column(name = "FORNAMN")
  var fornamn: String? = null

  @Column(name = "MELLANNAMN")
  var mellannamn: String? = null

  @Column(name = "EFTERNAMN")
  var efternamn: String? = null

  @Column(name = "BAKGRUND_NULAGE")
  var bakgrundNulage: String? = null
  @Column(name = "SEKRETESSMARKERING", nullable = false)

  var sekretessMarkering: Boolean

  init {
    this.personId = builder.personId ?: throw IllegalArgumentException("personId may not be null")
    this.fornamn = builder.fornamn
    this.mellannamn = builder.mellannamn
    this.efternamn = builder.efternamn
    this.bakgrundNulage = builder.bakgrundNulage
    this.sekretessMarkering = builder.sekretessMarkering
       ?: throw IllegalArgumentException("sekretessMarkering may not be null")
  }

  class Builder {
    var personId: String? = null
    var fornamn: String? = null
    var mellannamn: String? = null
    var efternamn: String? = null
    var bakgrundNulage: String? = null
    var sekretessMarkering: Boolean? = false

    fun personId(personId: String) = apply { this.personId = personId }
    fun fornamn(fornamn: String?) = apply { this.fornamn = fornamn }
    fun mellannamn(mellannamn: String?) = apply { this.mellannamn = mellannamn }
    fun efternamn(efternamn: String?) = apply { this.efternamn = efternamn }
    fun bakgrundNulage(bakgrundNulage: String?) = apply { this.bakgrundNulage = bakgrundNulage }
    fun sekretessMarkering(sekretessMarkering: Boolean) = apply { this.sekretessMarkering = sekretessMarkering }
    fun build() = InvanareEntity(this)
  }

  companion object Factory {

    fun toDomain(invanareEntity: InvanareEntity): Invanare {
      return Invanare(
         personId = Personnummer.createPersonnummer(invanareEntity.personId).get(),
         fornamn = invanareEntity.fornamn,
         mellannamn = invanareEntity.mellannamn,
         efternamen = invanareEntity.efternamn,
         bakgrundNulage = invanareEntity.bakgrundNulage,
         sektretessMarkering = invanareEntity.sekretessMarkering)
    }

    fun toEntity(invanare: Invanare): InvanareEntity {
      return InvanareEntity.Builder()
         .personId(personId = invanare.personId.personnummer)
         .fornamn(fornamn = invanare.fornamn)
         .mellannamn(mellannamn = invanare.mellannamn)
         .efternamn(efternamn = invanare.efternamen)
         .bakgrundNulage(bakgrundNulage = invanare.bakgrundNulage)
         .sekretessMarkering(sekretessMarkering = invanare.sektretessMarkering ?: false)
         .build()
    }
  }
}

