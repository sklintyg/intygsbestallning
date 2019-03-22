import se.inera.intyg.intygsbestallning.build.Config.Dependencies

plugins {
    kotlin("plugin.jpa")
    kotlin("kapt")
}

dependencies {
    // Project dependencies
    implementation(project(":common"))

    // External dependencies
    implementation(kotlin("reflect"))

    implementation("org.liquibase:liquibase-core:${Dependencies.liquibaseVersion}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.vavr:vavr:${Dependencies.vavrVersion}")
    implementation("com.querydsl:querydsl-core:${Dependencies.querydslVersion}")
    implementation("com.querydsl:querydsl-jpa:${Dependencies.querydslVersion}")

    // Embedded database for development
    runtime("com.h2database:h2:${Dependencies.h2Version}")

    kapt("com.querydsl:querydsl-apt:${Dependencies.querydslVersion}:jpa")
}
