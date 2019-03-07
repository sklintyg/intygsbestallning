#!groovy
node {
    def buildVersion = "0.0.1.${BUILD_NUMBER}"
    def infraVersion = "3.8.0.+"

    def java11tool = tool name: 'jdk11', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'
    def javaHome= "${java11tool}/jdk-11.0.2+9"

    def gradletool = tool name: 'gradle', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'
    def gradle = "${gradletool}/gradle-5.2.1/bin/gradle -Dorg.gradle.java.home=/${javaHome}"

    stage('build') {
            try {
                sh "${gradle} --refresh-dependencies clean build -P client"
              } finally {
                publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports/allTests', \
                    reportFiles: 'index.html', reportName: 'JUnit results'
            }
    }

    stage('tag and upload') {
        sh "${gradle} uploadArchives tagRelease -DbuildVersion=" + buildVersion + " -DinfraVersion=" + infraVersion
    }
}
