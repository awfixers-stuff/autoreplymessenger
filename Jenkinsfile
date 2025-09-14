pipeline {
    agent any

    tools {
        jdk 'jdk17'
    }

    environment {
        GRADLE_OPTS = '-Dorg.gradle.daemon=false'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            when {
                anyOf {
                    branch 'main'
                    branch 'develop'
                    changeRequest()
                }
            }
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew assembleDebug'
                archiveArtifacts artifacts: 'app/build/outputs/apk/debug/app-debug.apk', fingerprint: true
            }
        }

        stage('Test') {
            when {
                anyOf {
                    branch 'main'
                    branch 'develop'
                    changeRequest()
                }
            }
            steps {
                sh './gradlew testDebugUnitTest'
                junit 'app/build/test-results/testDebugUnitTest/*.xml'
                archiveArtifacts artifacts: 'app/build/reports/tests/', fingerprint: true
            }
        }

        stage('Code Quality') {
            when {
                anyOf {
                    branch 'main'
                    branch 'develop'
                    changeRequest()
                }
            }
            steps {
                sh './gradlew ktlintCheck'
                sh './gradlew detekt'
                sh './gradlew lintDebug'
                archiveArtifacts artifacts: 'app/build/reports/lint-results-debug.html, build/reports/detekt/', fingerprint: true
            }
        }

        stage('Dependency Check') {
            when {
                anyOf {
                    triggeredBy 'TimerTrigger'
                    branch 'main'
                }
            }
            steps {
                sh './gradlew dependencyUpdates'
                sh './gradlew dependencyUpdates -DoutputFormatter=json -DoutputDir=build/dependencyUpdates'
                archiveArtifacts artifacts: 'build/dependencyUpdates/', fingerprint: true
            }
        }

        stage('Release') {
            when {
                tag pattern: "v*", comparator: "REGEXP"
            }
            steps {
                sh './gradlew assembleRelease'
                sh './gradlew bundleRelease'
                archiveArtifacts artifacts: 'app/build/outputs/apk/release/app-release.apk, app/build/outputs/bundle/release/app-release.aab', fingerprint: true
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
