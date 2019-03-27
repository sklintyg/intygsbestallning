import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
  // Project dependencies
  implementation(project(":common"))

  implementation("se.inera.intyg.infra:hsa-integration:${extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:pu-integration:${extra["intygInfraVersion"]}")


  // External dependencies
  compile("org.apache.cxf:cxf-spring-boot-starter-jaxws:${Dependencies.cxfBootStarterVersion}")

  implementation("jakarta.jws:jakarta.jws-api:1.1.1")
  implementation("javax.xml.ws:jaxws-api:${Dependencies.jaxWsVersion}")
  implementation("javax.servlet:javax.servlet-api:${Dependencies.jaxServletApiVersion}")
}
