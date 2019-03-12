import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
    // Project dependencies
    compile("se.riv.intygsbestallning.certificate.order:intygsbestallning-certificate-order-schemas:${Dependencies.intygsbestallningCertificateOrderSchemasVersion}")

    // External dependencies
    implementation("com.fasterxml.jackson.core:jackson-databind:${Dependencies.jacksonVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Dependencies.jacksonVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${Dependencies.jacksonVersion}")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:${Dependencies.jacksonVersion}")

    implementation("io.vavr:vavr:${Dependencies.vavrVersion}")
    implementation("org.antlr:stringtemplate:${Dependencies.stringTemplateVersion}")

    compile("org.springframework.boot:spring-boot-starter-activemq:${Dependencies.springBootVersion}")
    compile("org.springframework.boot:spring-boot-starter-mail:${Dependencies.springBootVersion}")
}
