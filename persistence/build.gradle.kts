import se.inera.intyg.intygsbestallning.build.Config.Dependencies

plugins {
    kotlin("plugin.jpa") version "1.3.21"
}

dependencies {
    // Project dependencies
    implementation(project(":common"))

    // External dependencies
    implementation(kotlin("reflect"))

    implementation("org.liquibase:liquibase-core:${Dependencies.liquibaseVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Embedded database for development
    runtime("com.h2database:h2:${Dependencies.h2Version}")
}
