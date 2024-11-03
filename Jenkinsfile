pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'Docker-credentials'
        DOCKER_IMAGE = 'fedichebbi/course_devops'
        SLACK_CHANNEL = '#devopsjenkinspipline'
        SLACK_WEBHOOK_URL = 'https://hooks.slack.com/services/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'
    }

    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling...'
                git branch: 'fedichebbi',
                    url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git',
                    credentialsId: 'github-log'
            }
        }

        stage('Maven Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Mockito Test') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
            }
        }

        stage('SonarQube / Jacoco') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                sh 'mvn deploy -Dnexus.login=admin -Dnexus.password=nexus'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE .'
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        sh 'docker push $DOCKER_IMAGE'
                    }
                }
            }
        }

        stage('Docker Compose with Monitoring') {
            steps {
                echo 'Starting application and monitoring services with Docker Compose...'
                sh 'docker compose -f docker-compose.yml up -d'
            }
        }
    }

    post {
        success {
            script {
                sendSlackNotification("Job '${env.JOB_NAME}' (#${env.BUILD_NUMBER}) completed successfully.", 'good')
            }
        }
        failure {
            script {
                sendSlackNotification("Job '${env.JOB_NAME}' (#${env.BUILD_NUMBER}) failed.", 'danger')
            }
        }
    }
}

def sendSlackNotification(message, color) {
    def payload = "{\"text\": \"${message}\"}"
    sh """
    curl -X POST -H 'Content-type: application/json' --data '${payload}' '${SLACK_WEBHOOK_URL}'
    """
}
