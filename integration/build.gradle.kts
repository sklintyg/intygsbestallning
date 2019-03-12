import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
  // Project dependencies
  implementation(project(":common"))

  // External dependencies
  implementation("javax.xml.ws:jaxws-api:${Dependencies.jaxWsVersion}")
  implementation("javax.servlet:javax.servlet-api:${Dependencies.javaxServletApiVersion}")
  implementation("se.inera.intyg.infra:hsa-integration:0-SNAPSHOT")

  compile("org.apache.cxf:cxf-spring-boot-starter-jaxws:${Dependencies.cxfBootStarterVersion}")
}
