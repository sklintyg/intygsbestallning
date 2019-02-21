package se.inera.intyg.intygsbestallning.web.user

data class User(
   val id: Long = 1,
   val firstName: String = "Tolvan",
   val lastName: String = "Tolvansson",
   val address: Address = Address()
)

data class Address(
   val streetName: String = "TolvGatan",
   val streetNumber: Int = 12,
   val city: String = "Tolvköping",
   val country: Country = Country.TWELVELAND
)

enum class Country {
  SWEDEN,
  TWELVELAND
}
