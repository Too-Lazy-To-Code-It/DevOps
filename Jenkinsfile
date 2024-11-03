pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'Docker-credentials'
        DOCKER_IMAGE = 'fedichebbi/course_devops'
        SLACK_WEBHOOK_URL = "${env.SLACK_WEBHOOK_URL}" // Ensure this is set as an environment variable
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
                            "text": "Something went wrong during the deployment process. Please check the Jenkins console output for more details. :warning:\n*Summary:*\n- Docker Image: $DOCKER_IMAGE\n- Branch: fedichebbi\n\n*Immediate action required!*"
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
