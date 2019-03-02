pipeline {
    environment {
        buildVersion = "0.0.1.${BUILD_NUMBER}"
    }

    agent any

    stages {

        stage('build') {
            steps {
                shgradle "--refresh-dependencies clean build testReport sonarqube -PcodeQuality " +
                         "-DbuildVersion=" + buildVersion"
            }
            post {
                always {
                    publishHTML target: [
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'build/reports/allTests',
                        reportFiles: 'index.html',
                        reportName: 'JUnit results'
                    ]
                }
            }
        }

        stage('tag and upload') {
            steps {
                shgradle "uploadArchives tagRelease " +
                         "-DbuildVersion=" + buildVersion"
            }
        }
    }
}

