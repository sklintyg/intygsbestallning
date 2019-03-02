import se.inera.intyg.intygsbestallning.build.Config.Dependencies

plugins {
  id("org.springframework.boot") version "2.1.3.RELEASE"
}

dependencies {

  // Project dependencies
  compile(project(":persistence"))
  compile(project(":integration"))

  // Spring Boot starters
  implementation("org.springframework.boot:spring-boot-starter-web:${Dependencies.springBootVersion}")

  // Spring Boot test starters
  testImplementation("org.springframework.boot:spring-boot-starter-test:${Dependencies.springBootVersion}")
}