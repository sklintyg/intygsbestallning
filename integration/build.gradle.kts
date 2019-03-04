import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
  compile(project(":common"))

  implementation("se.riv.intygsbestallning.certificate.order:intygsbestallning-certificate-order-schemas:${Dependencies.intygsbestallningCertificateOrderSchemasVersion}")
  implementation("javax.xml.ws:jaxws-api:${Dependencies.jaxWsVersion}")
  implementation("javax.servlet:javax.servlet-api:${Dependencies.javaxServletApiVersion}")

  compile("org.apache.cxf:cxf-spring-boot-starter-jaxws:3.3.0")
}
