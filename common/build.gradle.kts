import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
    // Project dependencies
    compile("se.riv.intygsbestallning.certificate.order:intygsbestallning-certificate-order-schemas:${Dependencies.intygsbestallningCertificateOrderSchemasVersion}")

    // External dependencies
    compile("com.fasterxml.jackson.core:jackson-databind:${Dependencies.jacksonVersion}")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Dependencies.jacksonVersion}")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${Dependencies.jacksonVersion}")
    compile("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:${Dependencies.jacksonVersion}")

    compile("org.apache.commons:commons-lang3:${Dependencies.commonsLang3Version}")
    compile("io.vavr:vavr:${Dependencies.vavrVersion}")

    compile("org.springframework.boot:spring-boot-starter-activemq:${Dependencies.springBootVersion}")
    compile("org.springframework.boot:spring-boot-starter-mail:${Dependencies.springBootVersion}")
}
