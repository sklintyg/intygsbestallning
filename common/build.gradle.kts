import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
  // Project dependencies
  compile("se.riv.intygsbestallning.certificate.order:intygsbestallning-certificate-order-schemas:${Dependencies.intygsbestallningCertificateOrderSchemasVersion}")
  compile("se.inera.intyg.schemas:schemas-contract:${Dependencies.schemasContractVersion}")

  compile("org.apache.cxf:cxf-spring-boot-starter-jaxws:${Dependencies.cxfBootStarterVersion}")
  compile("org.apache.cxf:cxf-rt-frontend-jaxws:${Dependencies.cxfBootStarterVersion}") { isForce = true }
  compile("org.apache.cxf:cxf-rt-transports-http:${Dependencies.cxfBootStarterVersion}") { isForce = true }
  compile("org.apache.cxf:cxf-rt-features-logging:${Dependencies.cxfBootStarterVersion}") { isForce = true }

  // External dependencies
  compile("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
  compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
  compile("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
  compile("com.fasterxml.jackson.module:jackson-module-kotlin")

  compile("io.vavr:vavr:${Dependencies.vavrVersion}")

  compile("org.springframework.boot:spring-boot-starter-activemq")
  compile("org.springframework.boot:spring-boot-starter-mail")
}
