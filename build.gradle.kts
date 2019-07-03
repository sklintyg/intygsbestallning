
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import se.inera.intyg.IntygPluginCheckstyleExtension
import se.inera.intyg.JavaVersion
import se.inera.intyg.TagReleaseTask
import se.inera.intyg.intygsbestallning.build.Config.Dependencies
import se.inera.intyg.intygsbestallning.build.Config.Jvm
import se.inera.intyg.intygsbestallning.build.Config.TestDependencies

plugins {
  kotlin("jvm")
  maven
  `maven-publish`

  id("se.inera.intyg.plugin.common") apply false
  id("io.spring.dependency-management")
}

allprojects {
  group = "se.inera.intyg.intygsbestallning"
  version = System.getProperty("buildVersion", "0-SNAPSHOT")

  apply(plugin = "maven-publish")

  extra.apply {
    set("intygInfraVersion", System.getProperty("infraVersion", "0-SNAPSHOT"))
    set("refDataVersion", "1.0-SNAPSHOT")
    set("infraGroupId", "se.inera.intyg.infra-spring5")
    set("errorproneExclude", "true") //FIXME: Errorprone does not support Kotlin and KAPT. Until it does this will exclude the errorprone task for this project
    set("detekt", "true") // If '-P codeQuality' is set as a project property, this property activates the kotlin code analysis plugin Detekt
  }

  repositories {
    mavenLocal()
    maven {
      url = uri("https://build-inera.nordicmedtest.se/nexus/repository/releases/")
      mavenContent {
        releasesOnly()
      }
    }
    maven {
      url = uri("https://build-inera.nordicmedtest.se/nexus/repository/snapshots/")
      mavenContent {
        snapshotsOnly()
      }
    }
    mavenCentral()
    jcenter()
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
}

apply(plugin = "se.inera.intyg.plugin.common")

subprojects {
  apply(plugin = "org.gradle.maven")
  apply(plugin = "org.gradle.maven-publish")
  apply(plugin = "se.inera.intyg.plugin.common")
  apply(plugin = "kotlin")

  apply<DependencyManagementPlugin>()

  configure<IntygPluginCheckstyleExtension> {
    javaVersion = JavaVersion.JAVA11
    showViolations = true
    ignoreFailures = false
  }

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

    compileOnly("net.jcip:jcip-annotations:${Dependencies.jcipAnnotationsVersion}")
    compileOnly("com.github.spotbugs:spotbugs-annotations:${Dependencies.spotbugsAnnotationsVersion}")

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
    testImplementation("se.inera.intyg.refdata:refdata:${project.extra["refDataVersion"]}")

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

tasks {
  register<TagReleaseTask>("tagRelease")
}

dependencies {
  subprojects.forEach { archives(it) }
}
