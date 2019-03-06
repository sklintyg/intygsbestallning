
import com.moowork.gradle.node.npm.NpmTask
import se.inera.intyg.intygsbestallning.build.Config.Dependencies

val buildClient = project.hasProperty("client")

plugins {
  id("org.springframework.boot") version "2.1.3.RELEASE"
  id("com.moowork.node") version "1.2.0"
}

dependencies {

  // Project dependencies
  compile(project(":common"))
  compile(project(":integration"))
  compile(project(":persistence"))
  compile(project(":mail-sender"))

  // Spring Boot starters
  implementation("org.springframework.boot:spring-boot-starter-web:${Dependencies.springBootVersion}")

  implementation("javax.xml.ws:jaxws-api:${Dependencies.jaxWsVersion}")
  implementation("javax.servlet:javax.servlet-api:${Dependencies.javaxServletApiVersion}")

  // Spring Boot test starters
  testImplementation("org.springframework.boot:spring-boot-starter-test:${Dependencies.springBootVersion}")
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

  if (buildClient) {
    bootJar {
      from("client/build/") {
        into("static")
      }
    }

    "bootRun" {
      dependsOn(copyReactbuild)
    }

    "bootJar" {
      dependsOn(buildReactApp)
    }
  }
}
