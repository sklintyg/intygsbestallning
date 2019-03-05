import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
  implementation(project(":common"))
  implementation("javax.xml.ws:jaxws-api:${Dependencies.jaxWsVersion}")
  implementation("javax.servlet:javax.servlet-api:${Dependencies.javaxServletApiVersion}")

  compile("org.apache.cxf:cxf-spring-boot-starter-jaxws:3.3.0")
}
