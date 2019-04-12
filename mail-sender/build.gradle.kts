import se.inera.intyg.intygsbestallning.build.Config.Dependencies
import se.inera.intyg.intygsbestallning.build.Config.TestDependencies

dependencies {
    // Project dependencies
    implementation(project(":common"))

    // External dependencies
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.apache.camel:camel-spring-boot-starter:${Dependencies.camelBootStarterVersion}")

    implementation("org.apache.camel:camel-jackson:${Dependencies.camelVersion}")
    implementation("org.apache.camel:camel-spring-javaconfig:${Dependencies.camelVersion}")
    implementation("org.apache.camel:camel-jms:${Dependencies.camelVersion}")

    // External test dependencies
    testImplementation("com.jayway.awaitility:awaitility:${TestDependencies.awaitilityVersion}")
    testImplementation("org.apache.camel:camel-test-spring:${Dependencies.camelVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("junit:junit:${TestDependencies.junit4Version}")

    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
}

