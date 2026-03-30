pipeline {
    options {
        disableConcurrentBuilds()
    }
    agent {
        kubernetes {
            yaml """
                apiVersion: v1
                kind: Pod
                spec:
                  containers:
                  - name: runner
                    image: 356835234430.dkr.ecr.us-east-1.amazonaws.com/jdk-selenium:latest
                    command: ["tail", "-f", "/dev/null"]
                    securityContext:
                      runAsUser: 0
            """
        }
    }
    parameters {
        string(name: 'targetedEnv', defaultValue: '')
    }
    stages {

        stage('Install Browsers') {
            steps {
                container('runner') {
                    sh 'mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chrome"'
                }
            }
        }

        stage('Test') {
            environment {
                ENVIRONMENT = "${params.targetedEnv}"
            }
            steps {
                container('runner') {
                    sh 'mvn clean verify -Dtags=@smoke -DpostToTeams=true -Dbrowser=chromium -Dheadless=true -B -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn'
                    archiveArtifacts artifacts: 'test-output/**'
                }
            }
        }

        stage('Logs') {
            steps {
                containerLog('runner')
            }
        }
    }
}
