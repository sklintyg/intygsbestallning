package se.inera.intyg.intygsbestallning.build

object Config {

  object Jvm {
    const val sourceCompatibility = "11"
    const val targetCompatibility = "11"
    const val kotlinJvmTarget = "1.8"
    const val encoding = "UTF-8"
  }

  object Dependencies {
    const val springBootVersion = "2.1.3.RELEASE"
  }

  object TestDependencies {
    const val mockitoCoreVersion = "2.24.5"
    const val junitVersion = "5.4.0"
  }
}
