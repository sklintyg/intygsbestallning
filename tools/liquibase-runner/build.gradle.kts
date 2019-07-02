plugins {
    application
}

tasks {
    startScripts {
        mainClassName = "liquibase.integration.commandline.Main --driver=com.mysql.cj.jdbc.Driver --changeLogFile=changelog/db-changelog-master.xml"
        defaultJvmOpts = listOf("-Dfile.encoding=utf8")
    }
}

sonarqube {
    setSkipProject(true)
}

dependencies {
    runtime(project(":${rootProject.name}-persistence"))

    runtime("mysql:mysql-connector-java")
    runtime("org.liquibase:liquibase-core")
}

val liquibaseRunnerFileTar = file("$buildDir/distributions/${rootProject.name}-liquibase-runner-${rootProject.version}.tar")
val liquibaseRunnerTar = artifacts.add("archives", liquibaseRunnerFileTar) {
    type = "tar"
    builtBy("distTar")
}

val liquibaseRunnerFileZip = file("$buildDir/distributions/${rootProject.name}-liquibase-runner-${rootProject.version}.zip")
val liquibaseRunnerZip = artifacts.add("archives", liquibaseRunnerFileZip) {
    type = "zip"
    builtBy("distZip")
}

publishing {
    publications {
        create<MavenPublication>("liquibase") {
            artifact(liquibaseRunnerZip)
            artifact(liquibaseRunnerTar)
        }
    }
}
