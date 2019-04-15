import com.moowork.gradle.node.npm.NpmTask
import org.gradle.internal.os.OperatingSystem
import org.springframework.boot.gradle.tasks.bundling.BootJar
import se.inera.intyg.intygsbestallning.build.Config.Dependencies

// FIXME: Openshift build pipeline passes useMinifiedJavaScript to build (not client)
val buildClient = project.hasProperty("client") || project.hasProperty("useMinifiedJavaScript")

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

  implementation("${extra["infraGroupId"]}:hsa-integration:${extra["intygInfraVersion"]}")
  implementation("${extra["infraGroupId"]}:log-messages:${extra["intygInfraVersion"]}")
  implementation("${extra["infraGroupId"]}:monitoring:${extra["intygInfraVersion"]}")
  implementation("${extra["infraGroupId"]}:pu-integration:${extra["intygInfraVersion"]}")
  implementation("${extra["infraGroupId"]}:security-authorities:${extra["intygInfraVersion"]}")
  implementation("${extra["infraGroupId"]}:security-common:${extra["intygInfraVersion"]}")
  implementation("${extra["infraGroupId"]}:security-siths:${extra["intygInfraVersion"]}")
  implementation("${extra["infraGroupId"]}:security-filter:${extra["intygInfraVersion"]}")

  // External dependencies
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  //implementation("org.springframework.boot:spring-boot-starter-actuator")

  implementation("net.javacrumbs.shedlock:shedlock-spring:1.3.0")

  implementation("com.itextpdf:itext7-core:7.1.5")

  //api documentation
  implementation("io.springfox:springfox-swagger2:2.9.2")
  implementation("io.springfox:springfox-swagger-ui:2.9.2")


  implementation("jakarta.jws:jakarta.jws-api:1.1.1")
  implementation("javax.xml.ws:jaxws-api:${Dependencies.jaxVersion}")
  implementation("javax.servlet:javax.servlet-api:${Dependencies.jaxServletApiVersion}")

  implementation("org.springframework.security.extensions:spring-security-saml2-core:1.0.3.RELEASE")
  implementation("com.querydsl:querydsl-core:${Dependencies.querydslVersion}")

  // FIXME: shall not be bundled with app!
  implementation("se.inera.intyg.refdata:refdata:${extra["refDataVersion"]}")

}

tasks.getByName<BootJar>("bootJar") {
  manifest {
    attributes("Main-Class" to "org.springframework.boot.loader.PropertiesLauncher")
    attributes("Start-Class" to  "se.inera.intyg.intygsbestallning.web.IntygsbestallningApplication")
  }
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

    setEnvironment(mapOf("CI" to true))

    setArgs(listOf("run", "build"))
  }

  val testReactApp by creating(NpmTask::class) {
    dependsOn(npmInstall)

    setEnvironment(mapOf("CI" to true))

    setArgs(listOf("run", "test"))
  }

  val copyReactbuild by creating(Copy::class) {
    dependsOn(buildReactApp)

    from("client/build/")
    into("${project.buildDir}/resources/main/static")
  }

  val pathingJar by creating(Jar::class) {
    dependsOn(configurations.runtime)
    archiveAppendix.set("pathing")

    doFirst {
      manifest {
        val classpath = configurations.runtimeClasspath.get().files
           .map { it.toURI().toURL().toString().replaceFirst("file:/", "/") }
           .joinToString(separator = " ")

        val mainClass = "se.inera.intyg.intygsbestallning.web.IntygsbestallningApplication"

        attributes["Class-Path"] = classpath
        attributes["Main-Class"] = mainClass
      }
    }
  }

  if (OperatingSystem.current().isWindows) {
    bootRun {
      dependsOn(pathingJar)

      doFirst {

        classpath = files(
           "${project.projectDir}/build/classes/java/main",
           "${project.projectDir}/build/classes/kotlin/main",
           "${project.projectDir}/src/main/resources",
           "${project.projectDir}/build/main/resources",
           "${project.rootProject.projectDir}/src/main/resources",
           "${project.rootProject.projectDir}/devops/openshift/test/env",
           "${project.rootProject.projectDir}/devops/openshift/test/config",
           pathingJar.archiveFile)
      }
    }
  }

  bootRun {
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

    test {
      dependsOn(testReactApp)
    }
  }
}
