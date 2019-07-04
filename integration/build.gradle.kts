import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
  // Project dependencies
  implementation(project(":${rootProject.name}-common"))

  implementation("${project.extra["infraGroupId"]}:hsa-integration:${project.extra["intygInfraVersion"]}")
  implementation("${project.extra["infraGroupId"]}:pu-integration:${project.extra["intygInfraVersion"]}")


  // External dependencies
  implementation("jakarta.jws:jakarta.jws-api:${Dependencies.jakartaJwsVersion}")
  implementation("javax.xml.ws:jaxws-api:${Dependencies.jaxWsVersion}")
  implementation("javax.servlet:javax.servlet-api:${Dependencies.jaxServletApiVersion}")
}
