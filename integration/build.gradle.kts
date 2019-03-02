import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
  implementation("org.springframework:spring-context:${Dependencies.springVersion}")

  compile("com.sun.xml.ws:jaxws-ri:2.3.2")
  compile("se.riv.intygsbestallning.certificate.order:intygsbestallning-certificate-order-schemas:${Dependencies.intygsbestallningCertificateOrderSchemasVersion}")
}
