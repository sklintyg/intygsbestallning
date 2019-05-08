import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
  // Project dependencies
  implementation(project(":${rootProject.name}-common"))

  implementation("${extra["infraGroupId"]}:hsa-integration:${extra["intygInfraVersion"]}")
  implementation("${extra["infraGroupId"]}:pu-integration:${extra["intygInfraVersion"]}")


  // External dependencies
  implementation("jakarta.jws:jakarta.jws-api:${Dependencies.jakartaJwsVersion}")
  implementation("javax.xml.ws:jaxws-api:${Dependencies.jaxWsVersion}")
  implementation("javax.servlet:javax.servlet-api:${Dependencies.jaxServletApiVersion}")
}
