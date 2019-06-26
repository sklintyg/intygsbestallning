package se.inera.intyg.intygsbestallning.build

object Config {

  object Jvm {
    const val sourceCompatibility = "11"
    const val targetCompatibility = "11"
    const val kotlinJvmTarget = "1.8"
    const val encoding = "UTF-8"
  }

  object Dependencies {

    //Project dependencies
    const val intygPluginVersion = "3.0.7"
    const val intygsbestallningCertificateOrderSchemasVersion = "1.0.0-RELEASE"
    const val schemasContractVersion = "2.1.8"

    //External dependencies
    const val nodePluginVersion = "1.3.1"
    const val nodeVersion = "10.15.1"

    const val kotlinVersion = "1.3.31"

    const val springVersion = "5.1.7.RELEASE"
    const val springBootVersion = "2.1.5.RELEASE"
    const val springDependencyManagementVersion = "1.0.7.RELEASE"
    const val springSecuritySaml2Version = "1.0.3.RELEASE"

    const val camelVersion = "2.23.1"
    const val camelBootStarterVersion = "2.23.1"
    const val cxfBootStarterVersion = "3.3.0"
    const val guavaVersion = "27.1-jre"
    const val liquibaseVersion = "3.6.3"
    const val vavrVersion = "0.10.0"
    const val jaxServletApiVersion = "4.0.1"
    const val jaxVersion = "2.3.0"
    const val jaxWsVersion = "2.3.0"
    const val querydslVersion = "4.2.1"
    const val shedlockVersion = "1.3.0"
    const val itext7Version = "7.1.5"
    const val swaggerVersion = "2.9.2"
    const val jakartaJwsVersion = "1.1.1"

    // Spotbugs annotations
    const val spotbugsAnnotationsVersion = "3.1.12"
    const val jcipAnnotationsVersion = "1.0"
  }

  object TestDependencies {
    const val mockitoCoreVersion = "2.27.0"
    const val junit4Version = "4.12"
    const val junit5Version = "5.4.2"
    const val awaitilityVersion = "3.1.6"
    const val randomBeansVersion = "3.9.0"
    const val restAssuredVersion = "4.0.0"
    const val stAntlr4Version = "4.0.8"
  }
}
