import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
    compile("org.springframework.boot:spring-boot-starter-activemq:${Dependencies.springBootVersion}")
    compile("org.springframework.boot:spring-boot-starter-mail:${Dependencies.springBootVersion}")
}
