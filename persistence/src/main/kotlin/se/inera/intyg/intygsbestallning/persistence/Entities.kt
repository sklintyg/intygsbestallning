package se.inera.intyg.intygsbestallning.persistence

import javax.persistence.*

@Entity
@Table(name = "bestallning")
data class Bestallning(

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", nullable = false)
   val id: Long? = null

)
