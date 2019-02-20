import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "se.inera.intyg"
version = "0.0.1-SNAPSHOT"

plugins {
  id("org.springframework.boot").version("2.1.3.RELEASE")
  id("org.jetbrains.kotlin.jvm").version("1.2.71")

  application
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {

  // Kotlin
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  // Spring Boot starters
  implementation("org.springframework.boot:spring-boot-starter-web:2.1.3.RELEASE")

  // Kotlin
  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

  // Spring Boot test starters
  testImplementation("org.springframework.boot:spring-boot-starter-test:2.1.3.RELEASE")

}

application {
  // Define the main class for the application.
  mainClassName = "se.inera.intyg.intygsbestallning.IntygsbestallningApplicationKt"
}

tasks {
  withType<JavaCompile> {
    sourceCompatibility = "11"
    targetCompatibility = "11"
  }

  withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
  }
}
