import se.inera.intyg.intygsbestallning.build.Config.Dependencies
import se.inera.intyg.intygsbestallning.build.Config.TestDependencies

dependencies {
    // Project dependencies
    implementation(project(":common"))

    // External dependencies
    compile("org.springframework.boot:spring-boot-starter-mail")
    compile("org.springframework.boot:spring-boot-starter-activemq")
    compile("org.apache.camel:camel-spring-boot-starter:${Dependencies.camelBootStarterVersion}")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.apache.camel:camel-jackson:${Dependencies.camelVersion}")
    implementation("org.apache.camel:camel-spring-javaconfig:${Dependencies.camelVersion}")

    // Embedded database for development
    runtime("com.h2database:h2:${Dependencies.h2Version}")

    // External test dependencies
    testImplementation("com.jayway.awaitility:awaitility:${TestDependencies.awaitilityVersion}")
    testImplementation("org.apache.camel:camel-test-spring:${Dependencies.camelVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    test {
        exclude("**/*Integration*")
    }
}
