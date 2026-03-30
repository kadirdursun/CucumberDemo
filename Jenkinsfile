pipeline {
    agent any

    environment {
        MAVEN_OPTS = '-Xmx1024m'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Fix APT Sources') {
            steps {
                // Remove the Amazon Corretto repo whose GPG key has expired,
                // which causes `apt-get update` (triggered by playwright install --with-deps)
                // to fail with EXPKEYSIG A122542AB04F24E3.
                sh '''
                    rm -f /etc/apt/sources.list.d/corretto.list 2>/dev/null || true
                    sed -i '/apt\\.corretto\\.aws/d' /etc/apt/sources.list 2>/dev/null || true
                    apt-get update -qq || true
                '''
            }
        }

        stage('Build & Install Browsers') {
            steps {
                // Compiles code and triggers the exec-maven-plugin to run
                // `playwright install chromium` (bound to test-compile phase).
                sh 'mvn test-compile -B'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test -B'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'screenshot/**/*.png', allowEmptyArchive: true
                }
            }
        }

        stage('Logs') {
            steps {
                archiveArtifacts artifacts: 'target/**/*.json,target/**/*.html', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
