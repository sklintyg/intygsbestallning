#!groovy

node {
    def buildVersion = "1.1.0.${BUILD_NUMBER}"
    def infraVersion = "3.11.0.0.+"

    def versionFlags = "-DbuildVersion=${buildVersion} -DinfraVersion=${infraVersion}"

    stage('checkout') {
        git url: "https://github.com/sklintyg/intygsbestallning.git", branch: GIT_BRANCH
        util.run { checkout scm }
    }

    stage('build') {
        try {
            shgradle11 "--refresh-dependencies clean build -P client -P codeQuality testReport jacocoTestReport ${versionFlags}"
        } finally {
            publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports/allTests', \
                    reportFiles: 'index.html', reportName: 'JUnit results'
        }
    }

    stage('tag and upload') {
        shgradle11 "publish tagRelease ${versionFlags}"
    }

    stage('propagate') {
        gitRef = "v${buildVersion}"
        releaseFlag = "${GIT_BRANCH.startsWith("release")}"
        build job: "intygsbestallning-dintyg-build", wait: false, parameters: [
                [$class: 'StringParameterValue', name: 'BUILD_VERSION', value: buildVersion],
                [$class: 'StringParameterValue', name: 'INFRA_VERSION', value: infraVersion],
                [$class: 'StringParameterValue', name: 'GIT_REF', value: gitRef],
                [$class: 'StringParameterValue', name: 'RELEASE_FLAG', value: releaseFlag]
        ]
    }

    stage('notify') {
        util.notifySuccess()
    }
}
