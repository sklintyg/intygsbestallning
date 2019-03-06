pipeline {
    environment {
        buildVersion = "0.0.1.${BUILD_NUMBER}"
    }

    agent {
        docker {
            image "gradle:5.2.1-jdk11-slim"
            args "-v /var/lib/jenkins/jobs/intyg-intygsbeställning/workspace/build/reports/allTests:/home/gradle/build/reports/allTests" +
                 "-v /var/lib/jenkins/jobs/intyg-intygsbestallning/builds/${BUILD_NUMBER}/htmlreports:/home/gradle/build/${BUILD_NUMBER}/htmlreports"
        }
    }

    stages {
        stage('build') {
            steps {
                sh "gradle --refresh-dependencies clean build"
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
                sh "gradle uploadArchives tagRelease"
            }
        }
    }
}

