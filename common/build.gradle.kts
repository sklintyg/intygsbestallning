import se.inera.intyg.intygsbestallning.build.Config.Dependencies

dependencies {
    compile("se.riv.intygsbestallning.certificate.order:intygsbestallning-certificate-order-schemas:${Dependencies.intygsbestallningCertificateOrderSchemasVersion}")

    compile("org.springframework.boot:spring-boot-starter-activemq:${Dependencies.springBootVersion}")
    compile("org.springframework.boot:spring-boot-starter-mail:${Dependencies.springBootVersion}")
}
