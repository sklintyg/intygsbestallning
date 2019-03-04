import se.inera.intyg.intygsbestallning.build.Config.Dependencies
import se.inera.intyg.intygsbestallning.build.Config.TestDependencies

dependencies {

    implementation("org.apache.camel:camel-jackson:${Dependencies.camelVersion}")
    implementation("org.apache.camel:camel-spring-javaconfig:${Dependencies.camelVersion}")

    testImplementation("com.jayway.awaitility:awaitility:${TestDependencies.awaitilityVersion}")
    testImplementation("org.apache.camel:camel-test-spring:${Dependencies.camelVersion}")

    runtime("com.h2database:h2:${Dependencies.h2Version}")

    compile(project(":common"))
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")
    compile("org.apache.camel:camel-spring-boot-starter:${Dependencies.camelBootStarterVersion}")
    compile("org.springframework.boot:spring-boot-starter-mail:${Dependencies.springBootVersion}")
    compile("org.springframework.boot:spring-boot-starter-activemq:${Dependencies.springBootVersion}")

    testCompile("org.springframework.boot:spring-boot-starter-test:${Dependencies.springBootVersion}")
}

tasks {
    test {
        exclude("**/*Integration*")
    }
}
