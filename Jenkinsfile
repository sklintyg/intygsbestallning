pipeline {
    environment {
        buildVersion = "0.0.1.${BUILD_NUMBER}"
    }

    agent {
        docker { image "adoptopenjdk/openjdk11" }
    }
    stages {

        stage('build') {
            steps {
                shgradle "--refresh-dependencies clean build -P client"
            }
            post {
                always {
                    publishHTML target: [
                        allowMissing: true,
                        alwaysLinkToLastBuild   : true,
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
                shgradle "uploadArchives tagRelease"
            }
        }
    }
}

