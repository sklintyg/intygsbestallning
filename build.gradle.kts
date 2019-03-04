import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import se.inera.intyg.TagReleaseTask
import se.inera.intyg.intygsbestallning.build.Config.Jvm
import se.inera.intyg.intygsbestallning.build.Config.Dependencies
import se.inera.intyg.intygsbestallning.build.Config.TestDependencies

plugins {
  id("org.gradle.maven")
  id("org.gradle.maven-publish")
  id("org.jetbrains.kotlin.jvm") version "1.3.21"
  id("se.inera.intyg.plugin.common") version "2.0.3" apply false
}

allprojects {
  group = "se.inera.intyg.intygsbestallning"
  version = System.getProperty("buildVersion") ?: "0-SNAPSHOT"

  repositories {
    mavenLocal()
    maven("https://build-inera.nordicmedtest.se/nexus/repository/releases/")
    maven("http://repo.maven.apache.org/maven2")
  }
}

apply(plugin = "se.inera.intyg.plugin.common")

subprojects {
  apply(plugin = "org.gradle.maven")
  apply(plugin = "org.gradle.maven-publish")
  apply(plugin = "se.inera.intyg.plugin.common")
  apply(plugin = "org.jetbrains.kotlin.jvm")

  dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework:spring-context:${Dependencies.springVersion}")
    implementation("com.google.guava:guava:${Dependencies.guavaVersion}")

    testCompile("org.mockito:mockito-core:${TestDependencies.mockitoCoreVersion}")
    testCompile("org.junit.jupiter:junit-jupiter-api:${TestDependencies.junitVersion}")
  }

  tasks {
    withType<JavaCompile> {
      sourceCompatibility = Jvm.sourceCompatibility
      targetCompatibility = Jvm.targetCompatibility
      options.encoding = Jvm.encoding
    }

    withType<KotlinCompile> {
      kotlinOptions.jvmTarget = Jvm.kotlinJvmTarget
    }

    withType<TagReleaseTask>()
  }
}

publishing {
  repositories {
    maven {
      url = uri("https://build-inera.nordicmedtest.se/nexus/repository/releases/")
      credentials {
        username = System.getProperty("nexusUsername")
        password = System.getProperty("nexusPassword")
      }
    }
  }
}

dependencies {
  subprojects.forEach { archives(it) }
}
