pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'Docker-credentials'
        DOCKER_IMAGE = 'fedichebbi/course_devops'
        SLACK_WEBHOOK_URL = "${env.SLACK_WEBHOOK_URL}"
    }

    stages {
        stage('Checkout GIT') {
            steps {
                script {
                    currentStage = 'Checkout GIT'
                }
                echo 'Pulling...'
                git branch: 'fedichebbi',
                    url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git',
                    credentialsId: 'github-log'
            }
        }

        stage('Maven Compile') {
            steps {
                script {
                    currentStage = 'Maven Compile'
                }
                sh 'mvn compile'
            }
        }

        stage('Mockito Test') {
            steps {
                script {
                    currentStage = 'Mockito Test'
                }
                echo 'Running tests...'
                sh 'mvn test'
            }
        }

        stage('SonarQube / Jacoco') {
            steps {
                script {
                    currentStage = 'SonarQube / Jacoco'
                }
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    currentStage = 'Deploy to Nexus'
                }
                echo 'Deploying to Nexus...'
                sh 'mvn deploy -Dnexus.login=${NEXUS_USERNAME} -Dnexus.password=${NEXUS_PASSWORD}'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    currentStage = 'Build Docker Image'
                }
                sh 'docker build -t $DOCKER_IMAGE .'
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    currentStage = 'Push Docker Image'
                }
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        sh 'docker push $DOCKER_IMAGE'
                    }
                }
            }
        }

        stage('Docker Compose with Monitoring') {
            steps {
                script {
                    currentStage = 'Docker Compose with Monitoring'
                }
                echo 'Starting application and monitoring services with Docker Compose...'
                sh 'docker compose -f docker-compose.yml up -d'
            }
        }
    }

    post {
        success {
            script {
                def message = """
                {
                    "text": "🚀 *Deployment Successful!* :tada:",
                    "attachments": [
                        {
                            "color": "#36a64f",
                            "text": "Your application has been deployed successfully. :white_check_mark:\n*Summary:*\n- Docker Image: $DOCKER_IMAGE\n- Branch: fedichebbi\n\n*Thank you for your patience!*"
                        }
                    ]
                }
                """
                sh """
                curl -X POST -H 'Content-type: application/json' --data '${message}' '${SLACK_WEBHOOK_URL}'
                """
            }
        }
        failure {
            script {
                def message = """
                {
                    "text": "❌ *Deployment Failed!* :x:",
                    "attachments": [
                        {
                            "color": "#ff0000",
                            "text": "Something went wrong during the *${currentStage}* stage. Please check the Jenkins console output for more details. :warning:\n*Summary:*\n- Docker Image: $DOCKER_IMAGE\n- Branch: fedichebbi\n\n*Immediate action required!*"
                        }
                    ]
                }
                """
                sh """
                curl -X POST -H 'Content-type: application/json' --data '${message}' '${SLACK_WEBHOOK_URL}'
                """
            }
        }
    }
}
