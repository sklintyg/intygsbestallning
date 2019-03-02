import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
  implementation("org.springframework:spring-context:${Dependencies.springVersion}")
  implementation("javax.xml.ws:jaxws-api:${Dependencies.jaxWsVersion}")
  compile("se.riv.intygsbestallning.certificate.order:intygsbestallning-certificate-order-schemas:${Dependencies.intygsbestallningCertificateOrderSchemasVersion}")
}
