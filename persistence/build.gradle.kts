import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {

    // embedded database for dev
    runtime("com.h2database:h2:${Dependencies.h2Version}")

    implementation("org.jetbrains.kotlin:kotlin-reflect:${Dependencies.kotlinVersion}")
    implementation("org.liquibase:liquibase-core:${Dependencies.liquibaseVersion}")
    
    compile("org.springframework.boot:spring-boot-starter-data-jpa:${Dependencies.springBootVersion}")
}
