plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.21")
}

repositories {
    mavenLocal()
    maven("https://build-inera.nordicmedtest.se/nexus/repository/releases/")
    maven("http://repo.maven.apache.org/maven2")
}
