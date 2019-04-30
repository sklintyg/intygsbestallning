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
    const val intygPluginVersion = "3.1.0-SNAPSHOT"
    const val intygsbestallningCertificateOrderSchemasVersion = "1.0-RC7"
    const val schemasContractVersion = "2.1.8"

    //External dependencies
    const val nodePluginVersion = "1.2.0"
    const val nodeVersion = "10.15.1"

    const val kotlinVersion = "1.3.30"
    const val springVersion = "5.1.6.RELEASE"
    const val springBootVersion = "2.1.4.RELEASE"
    const val springDependencyManagementVersion = "1.0.7.RELEASE"

    const val activemqVersion = "5.13.5"
    const val camelVersion = "2.23.1"
    const val camelBootStarterVersion = "2.23.1"
    const val commonsLang3Version = "3.7"
    const val cxfBootStarterVersion = "3.3.0"
    const val guavaVersion = "27.1-jre"
    const val liquibaseVersion = "3.6.3"
    const val jacksonVersion = "2.9.4"
    const val javaxJmsVersion = "1.1-rev-1"
    const val javaxMailVersion = "1.5.2"
    const val vavrVersion = "0.10.0"
    const val jaxServletApiVersion = "4.0.1"
    const val jaxVersion = "2.3.0"
    const val jaxWsVersion = "2.3.0"
    const val querydslVersion = "4.2.1"
    const val shedlockVersion = "1.3.0"
    const val itext7Version = "7.1.5"
    const val swaggerVersion = "2.9.2"
    const val jakartaJwsVersion = "1.1.1"
    const val springSecuritySaml2Version = "1.0.3.RELEASE"

  }

  object TestDependencies {
    const val mockitoCoreVersion = "2.24.5"
    const val junit4Version = "4.12"
    const val junit5Version = "5.4.1"
    const val awaitilityVersion = "1.4.0"
    const val dbUnitVersion = "2.4.9"
    const val springockitoVersion = "1.0.8"
    const val randomBeansVersion = "3.9.0"
    const val restAssuredVersion = "2.9.0"
    const val stAntlr4Version = "4.0.8"
  }
}
