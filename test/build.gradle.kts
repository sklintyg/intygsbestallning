import com.moowork.gradle.node.task.NodeTask

plugins {
  id("com.moowork.node") version "1.2.0"
}

tasks.clean {
  delete("test-results")
}

tasks {
  node {
    version = "10.15.1"
    download = true
    distBaseUrl = "https://build-inera.nordicmedtest.se/node/"
    nodeModulesDir = file("${project.projectDir}")
  }
}

tasks.register<NodeTask>("cypressTest") {
  dependsOn("npmInstall")

  val baseUrl = project.findProperty("baseUrl") ?: "http://localhost:8080"

  setScript(file( "scripts/run.js" ))

  setArgs(listOf("baseUrl=${baseUrl}"))
}
