import com.moowork.gradle.node.npm.NpmTask
import se.inera.intyg.intygsbestallning.build.Config.Dependencies

val buildClient = project.hasProperty("client")

plugins {
  id("org.springframework.boot") version "2.1.3.RELEASE"
  id("com.moowork.node") version "1.2.0"
}

dependencies {
  // Project dependencies
  implementation(project(":common"))
  implementation(project(":integration"))
  implementation(project(":persistence"))
  implementation(project(":mail-sender"))

  implementation("se.inera.intyg.infra:hsa-integration:${extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:log-messages:${extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:monitoring:${extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:pu-integration:${extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:security-authorities:${extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:security-common:${extra["intygInfraVersion"]}")
  implementation("se.inera.intyg.infra:security-siths:${extra["intygInfraVersion"]}")

  // External dependencies
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-redis")

  implementation("jakarta.jws:jakarta.jws-api:1.1.1")
  implementation("javax.xml.ws:jaxws-api:${Dependencies.jaxVersion}")
  implementation("javax.servlet:javax.servlet-api:${Dependencies.jaxServletApiVersion}")
  implementation("org.antlr:stringtemplate:${Dependencies.stringTemplateVersion}")

  implementation("org.springframework.security.extensions:spring-security-saml2-core:1.0.3.RELEASE")

  compile("org.springframework.boot:spring-boot-starter-actuator")
  //compile("org.springframework.boot:spring-boot-starter-actuator:${Dependencies.springBootVersion}")

  // Test dependencies
  testImplementation(kotlin("test"))
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.clean {
  delete("client/build")
}

tasks {
  node {
    version = "10.15.1"
    download = true
    distBaseUrl = "https://build-inera.nordicmedtest.se/node/"
    nodeModulesDir = file("${project.projectDir}/client")
  }

  val buildReactApp by creating(NpmTask::class) {
    dependsOn(npmInstall)

    setArgs(listOf("run", "build"))
  }

  val copyReactbuild by creating(Copy::class) {
    dependsOn(buildReactApp)

    from("client/build/")
    into("${project.buildDir}/resources/main/static")
  }

  bootRun {
    systemProperties = mapOf("resource.dir" to "${project.projectDir}/../src/main/resources")

  }

  if (buildClient) {
    bootJar {
      from("client/build/") {
        into("static")
      }
    }

    bootRun {
      dependsOn(copyReactbuild)
    }

    bootJar {
      dependsOn(buildReactApp)
    }
  }

}
