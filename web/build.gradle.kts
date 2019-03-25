import com.moowork.gradle.node.npm.NpmTask
import org.gradle.internal.os.OperatingSystem
import se.inera.intyg.intygsbestallning.build.Config.Dependencies

val buildClient = project.hasProperty("client")

plugins {
  id("org.springframework.boot") version "2.1.3.RELEASE"
  id("com.moowork.node") version "1.2.0"
}

dependencies {
  // Project dependencies
  implementation(project(":common"))
  compile(project(":integration"))
  implementation(project(":persistence"))
  implementation(project(":mail-sender"))

  compile("se.inera.intyg.infra:hsa-integration:${extra["intygInfraVersion"]}")
  compile("se.inera.intyg.infra:log-messages:${extra["intygInfraVersion"]}")
  compile("se.inera.intyg.infra:monitoring:${extra["intygInfraVersion"]}")
  compile("se.inera.intyg.infra:pu-integration:${extra["intygInfraVersion"]}")
  compile("se.inera.intyg.infra:security-authorities:${extra["intygInfraVersion"]}")
  compile("se.inera.intyg.infra:security-common:${extra["intygInfraVersion"]}")
  compile("se.inera.intyg.infra:security-siths:${extra["intygInfraVersion"]}")

  // External dependencies
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  //implementation("org.springframework.boot:spring-boot-starter-actuator")

  implementation("jakarta.jws:jakarta.jws-api:1.1.1")
  implementation("javax.xml.ws:jaxws-api:${Dependencies.jaxVersion}")
  implementation("javax.servlet:javax.servlet-api:${Dependencies.jaxServletApiVersion}")
  implementation("org.antlr:stringtemplate:${Dependencies.stringTemplateVersion}")

  implementation("org.springframework.security.extensions:spring-security-saml2-core:1.0.3.RELEASE")
  implementation("com.querydsl:querydsl-core:${Dependencies.querydslVersion}")
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
    systemProperties = mapOf(
       "resource.dir" to "${project.rootProject.projectDir}/src/main/resources",
       "certificate.folder" to "${project.rootProject.projectDir}/devops/openshift/test/env",
       "config.dir" to "${project.rootProject.projectDir}/devops/openshift/test/config",
       "credentials.file" to "${project.rootProject.projectDir}/devops/openshift/test/env/secret-env.properties"
    )
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
