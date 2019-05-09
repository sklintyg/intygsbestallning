import com.moowork.gradle.node.task.NodeTask
import se.inera.intyg.intygsbestallning.build.Config.Dependencies

plugins {
  id("com.moowork.node")
}

node {
  version = Dependencies.nodeVersion
  download = true
  distBaseUrl = "https://build-inera.nordicmedtest.se/node/"
  nodeModulesDir = file("${project.projectDir}")
}

tasks {
  clean {
    delete("build/test-results")
  }
  register<NodeTask>("cypressTest") {
    dependsOn("npmInstall")

    val baseUrl = System.getProperty("baseUrl", "http://localhost:8080")

    setScript(file("scripts/run.js"))

    setArgs(listOf("baseUrl=${baseUrl}"))
  }
}

