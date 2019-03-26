import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
    // Project dependencies
    compile("se.riv.intygsbestallning.certificate.order:intygsbestallning-certificate-order-schemas:${Dependencies.intygsbestallningCertificateOrderSchemasVersion}")
    compile("se.inera.intyg.schemas:schemas-contract:${Dependencies.schemasContractVersion}")

    // External dependencies
    compile("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin")


    compile("io.vavr:vavr:${Dependencies.vavrVersion}")
    implementation("org.antlr:stringtemplate:${Dependencies.stringTemplateVersion}")

    implementation("org.springframework.boot:spring-boot-starter-activemq")
    implementation("org.springframework.boot:spring-boot-starter-mail")
}
