import java.lang.IllegalArgumentException

pluginManagement {
	repositories {
		maven("https://build-inera.nordicmedtest.se/nexus/repository/releases/")
		gradlePluginPortal()
	}
}
rootProject.name = "intygsbestallning"

include(":web")
include(":persistence")

fun getProjectDirName(project: String): String {
	return when(project) {
		"web" ->"$rootDir/web"
		"persistence" ->"$rootDir/persistence"
		else -> throw IllegalArgumentException("Project module $project does not exist.")
	}
}

for (project in rootProject.children) {
	val projectName = project.name

	project.projectDir = file(getProjectDirName(projectName))
	project.buildFileName = "build.gradle.kts"

	if (!project.projectDir.isDirectory) {
		throw IllegalArgumentException("Project directory ${project.projectDir} for project ${project.name} does not exist.")
	}

	if (!project.buildFile.isFile) {
		throw IllegalArgumentException("Build file ${project.buildFile} for project ${project.name} does not exist.")
	}
}
