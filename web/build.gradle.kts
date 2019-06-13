import com.moowork.gradle.node.npm.NpmTask
import org.gradle.internal.os.OperatingSystem
import se.inera.intyg.intygsbestallning.build.Config.Dependencies
import se.inera.intyg.intygsbestallning.build.Config.TestDependencies

val buildClient = project.hasProperty("client")
val localBuild = project.gradle.startParameter.taskNames.contains("bootRun")

plugins {
  id("org.springframework.boot")
  id("com.moowork.node")
}

dependencies {
  // Project dependencies
  implementation(project(":${rootProject.name}-common"))
  implementation(project(":${rootProject.name}-integration"))
  implementation(project(":${rootProject.name}-persistence"))
  implementation(project(":${rootProject.name}-mail-sender"))

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
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.session:spring-session-data-redis")

  implementation("net.javacrumbs.shedlock:shedlock-spring:${Dependencies.shedlockVersion}")

  implementation("com.itextpdf:itext7-core:${Dependencies.itext7Version}")

  //api documentation
  implementation("io.springfox:springfox-swagger2:${Dependencies.swaggerVersion}")
  implementation("io.springfox:springfox-swagger-ui:${Dependencies.swaggerVersion}")


  implementation("jakarta.jws:jakarta.jws-api:${Dependencies.jakartaJwsVersion}")
  implementation("javax.xml.ws:jaxws-api:${Dependencies.jaxVersion}")
  implementation("javax.servlet:javax.servlet-api:${Dependencies.jaxServletApiVersion}")

  implementation("org.springframework.security.extensions:spring-security-saml2-core:${Dependencies.springSecuritySaml2Version}")
  implementation("com.querydsl:querydsl-core:${Dependencies.querydslVersion}")

  if (localBuild) {
    implementation("se.inera.intyg.refdata:refdata:${extra["refDataVersion"]}")
  }

  testImplementation("io.rest-assured:rest-assured:${TestDependencies.restAssuredVersion}")
  testImplementation("io.rest-assured:json-schema-validator:${TestDependencies.restAssuredVersion}")
  testImplementation("io.rest-assured:json-schema-validator:${TestDependencies.restAssuredVersion}")
  testImplementation("io.rest-assured:json-path:${TestDependencies.restAssuredVersion}")
  testImplementation("io.rest-assured:xml-path:${TestDependencies.restAssuredVersion}")
  testImplementation("org.antlr:ST4:${TestDependencies.stAntlr4Version}")

}

node {
  version = Dependencies.nodeVersion
  download = true
  distBaseUrl = "https://build-inera.nordicmedtest.se/node/"
  nodeModulesDir = file("${project.projectDir}/client")
}

springBoot {
  buildInfo()
}

tasks {

  val buildReactApp by creating(NpmTask::class) {
    dependsOn(npmInstall)

    setEnvironment(mapOf("CI" to true))

    setArgs(listOf("run", "build"))
  }

  val testReactApp by creating(NpmTask::class) {
    dependsOn(npmInstall)

    setEnvironment(mapOf("CI" to true))

    setArgs(listOf("run", "test", "--", "--coverage"))
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

  val restAssuredTest by creating(Test::class) {
    outputs.upToDateWhen { false }
    systemProperty("integration.tests.baseUrl", System.getProperty("baseUrl", "http://localhost:8080"))
    include("**/*IT*")
  }

  test {
    exclude("**/*IT*")
  }

  bootJar {
    manifest {
      attributes("Main-Class" to "org.springframework.boot.loader.PropertiesLauncher")
      attributes("Start-Class" to  "se.inera.intyg.intygsbestallning.web.IntygsbestallningApplication")
    }
  }

  clean {
    delete("client/build")
  }

  if (OperatingSystem.current().isWindows) {
    bootRun {
      dependsOn(pathingJar)

      doFirst {
        classpath = files(
           "${project.projectDir}/build/classes/java/main",
           "${project.projectDir}/build/classes/kotlin/main",
           "${project.projectDir}/src/main/resources",
           "${project.projectDir}/build/resources/main",
           pathingJar.archiveFile)
      }
    }
  }

  if (buildClient) {

    bootJar {
      dependsOn(buildReactApp)
      from("client/build/") {
        into("static")
      }
    }

    bootRun {
      dependsOn(copyReactbuild)
    }

    test {
      dependsOn(testReactApp)
    }
  }
  
  bootRun {
      jvmArgs = listOf("-Dconfig.folder=${project.rootProject.projectDir}/devops/dev")
  }
}
