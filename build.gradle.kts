
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import se.inera.intyg.TagReleaseTask
import se.inera.intyg.intygsbestallning.build.Config.Dependencies
import se.inera.intyg.intygsbestallning.build.Config.Jvm
import se.inera.intyg.intygsbestallning.build.Config.TestDependencies

plugins {
  kotlin("jvm")
  maven
  `maven-publish`

  id("se.inera.intyg.plugin.common") version "2.0.3" apply false
  id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

allprojects {
  group = "se.inera.intyg.intygsbestallning"
  version = System.getenv("buildVersion") ?: "0-SNAPSHOT"

  extra.apply {
    set("intygInfraVersion", System.getenv("infraVersion") ?: "0-SNAPSHOT")
    set("refDataVersion", System.getenv("refDataVersion") ?: "1.0-SNAPSHOT")
    set("infraGroupId", "se.inera.intyg.infra-spring5")
  }

  repositories {
    mavenLocal()
    mavenCentral()
    maven("https://build-inera.nordicmedtest.se/nexus/repository/snapshots/")
    maven("https://build-inera.nordicmedtest.se/nexus/repository/releases/")
  }
}

apply(plugin = "se.inera.intyg.plugin.common")

subprojects {
  apply(plugin = "org.gradle.maven")
  apply(plugin = "org.gradle.maven-publish")
  apply(plugin = "se.inera.intyg.plugin.common")
  apply(plugin = "kotlin")

  apply<DependencyManagementPlugin>()

  dependencyManagement {
    imports {
      mavenBom("org.springframework:spring-framework-bom:${Dependencies.springVersion}")
      mavenBom("org.springframework.boot:spring-boot-dependencies:${Dependencies.springBootVersion}") {
        bomProperty("kotlin.version", Dependencies.kotlinVersion)
      }
      mavenBom("org.junit:junit-bom:${TestDependencies.junit5Version}")
    }
  }

  dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    compile("javax.xml.bind:jaxb-api:${Dependencies.jaxVersion}")
    compile("com.sun.xml.bind:jaxb-core:${Dependencies.jaxVersion}")
    compile("com.sun.xml.bind:jaxb-impl:${Dependencies.jaxVersion}")

    implementation(kotlin("stdlib-jdk8"))

    implementation("org.springframework:spring-context")

    implementation("com.google.guava:guava:${Dependencies.guavaVersion}")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.platform:junit-platform-runner")
    testImplementation("io.github.benas:random-beans:${TestDependencies.randomBeansVersion}")
    testImplementation("org.mockito:mockito-core:${TestDependencies.mockitoCoreVersion}")
    testImplementation("org.mockito:mockito-junit-jupiter:${TestDependencies.mockitoCoreVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("se.inera.intyg.refdata:refdata:${extra["refDataVersion"]}")

    testImplementation(kotlin("test"))

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  }

  tasks {
    withType<Test> {
      useJUnitPlatform()
    }

    withType<JavaCompile> {
      sourceCompatibility = Jvm.sourceCompatibility
      targetCompatibility = Jvm.targetCompatibility
      options.encoding = Jvm.encoding
    }

    withType<KotlinCompile> {
      kotlinOptions.jvmTarget = Jvm.kotlinJvmTarget
    }
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

tasks.register<TagReleaseTask>("tagRelease") {

}
