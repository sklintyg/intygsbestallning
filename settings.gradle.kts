import se.inera.intyg.intygsbestallning.build.Config.Dependencies

pluginManagement {
	repositories {
		maven("https://build-inera.nordicmedtest.se/nexus/repository/releases/")
		gradlePluginPortal()
	}
	resolutionStrategy {
		eachPlugin {
			if (requested.id.id.startsWith("org.jetbrains.kotlin.")) {
				useVersion(Dependencies.kotlinVersion)
			}
			if (requested.id.id.startsWith("se.inera.intyg.plugin.common")) {
				useVersion(Dependencies.intygPluginVersion)
			}
			if (requested.id.id.startsWith("io.spring.dependency-management")) {
				useVersion(Dependencies.springDependencyManagementVersion)
			}
		}
	}
}
rootProject.name = "intygsbestallning"

include(":common")
include(":mail-sender")
include(":integration")
include(":persistence")
include(":web")
include(":test")

fun getProjectDirName(project: String): String {
	return when(project) {
		"common" ->"$rootDir/common"
		"mail-sender" ->"$rootDir/mail-sender"
		"integration" ->"$rootDir/integration"
		"persistence" ->"$rootDir/persistence"
		"web" ->"$rootDir/web"
		"test" ->"$rootDir/test"
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
