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
    const val springVersion = "5.1.5.RELEASE"
    const val springBootVersion = "2.1.3.RELEASE"

    const val activemqVersion = "5.13.5"
    const val camelVersion = "2.16.0"
    const val camelBootStarterVersion = "2.17.0"
    const val commonsLang3Version = "3.7"
    const val cxfBootStarterVersion = "3.3.0"
    const val guavaVersion = "27.0.1-jre"
    const val h2Version = "1.4.198"
    const val liquibaseVersion = "3.6.3"
    const val jacksonVersion = "2.9.4"
    const val javaxJmsVersion = "1.1-rev-1"
    const val javaxMailVersion = "1.5.2"
    const val javaxServletApiVersion = "4.0.1"
    const val jaxWsVersion = "2.2.3"

    const val intygsbestallningCertificateOrderSchemasVersion = "1.0-RC6"
  }

  object TestDependencies {
    const val mockitoCoreVersion = "2.24.5"
    const val junitVersion = "5.4.0"
    const val awaitilityVersion = "1.4.0"
    const val dbUnitVersion = "2.4.9"
    const val springockitoVersion = "1.0.8"
  }
}
