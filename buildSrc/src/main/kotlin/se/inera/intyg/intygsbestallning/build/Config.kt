package se.inera.intyg.intygsbestallning.build

object Config {

  object Jvm {
    const val sourceCompatibility = "11"
    const val targetCompatibility = "11"
    const val kotlinJvmTarget = "1.8"
    const val encoding = "UTF-8"
  }

  object Dependencies {
    const val kotlinVersion = "1.3.21"
    const val springBootVersion = "2.1.3.RELEASE"
    const val springVersion = "5.1.5.RELEASE"
    const val h2Version = "1.4.198"
    const val liquibaseVersion = "3.6.3"
    const val intygsbestallningCertificateOrderSchemasVersion = "1.0-RC5"
    const val jaxWsVersion = "2.3.1"
  }

  object TestDependencies {
    const val mockitoCoreVersion = "2.24.5"
    const val junitVersion = "5.4.0"
  }
}
