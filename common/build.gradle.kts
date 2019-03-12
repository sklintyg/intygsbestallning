import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
    // Project dependencies
    compile("se.riv.intygsbestallning.certificate.order:intygsbestallning-certificate-order-schemas:${Dependencies.intygsbestallningCertificateOrderSchemasVersion}")

    // External dependencies
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")

    implementation("io.vavr:vavr:${Dependencies.vavrVersion}")
    implementation("org.antlr:stringtemplate:${Dependencies.stringTemplateVersion}")

    implementation("org.springframework.boot:spring-boot-starter-activemq")
    implementation("org.springframework.boot:spring-boot-starter-mail")
}
