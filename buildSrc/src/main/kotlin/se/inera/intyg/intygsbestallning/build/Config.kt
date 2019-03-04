package se.inera.intyg.intygsbestallning.build

object Config {

  object Jvm {
    const val sourceCompatibility = "11"
    const val targetCompatibility = "11"
    const val kotlinJvmTarget = "1.8"
    const val encoding = "UTF-8"
  }

  object Dependencies {
    const val activemqVersion = "5.13.5"
    const val camelVersion = "2.16.0"
    const val kotlinVersion = "1.3.21"
    const val springBootVersion = "2.1.3.RELEASE"
    const val camelBootStarterVersion = "2.17.0"
    const val springVersion = "5.1.5.RELEASE"
    const val h2Version = "1.4.198"
    const val liquibaseVersion = "3.6.3"
    const val intygsbestallningCertificateOrderSchemasVersion = "1.0-RC5"
    const val jaxWsVersion = "2.3.1"
    const val javaxMailVersion = "1.5.2"

  }

  object TestDependencies {
    const val mockitoCoreVersion = "2.24.5"
    const val junitVersion = "5.4.0"
    const val awaitilityVersion = "1.4.0"
    const val dbUnitVersion = "2.4.9"
    const val springockitoVersion = "1.0.8"
    const val googleGuavaVersion = "24.1-jre"

  }
}
