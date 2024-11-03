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
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    echo 'Pulling...'
                    git branch: 'fedicbi',
                        url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git',
                        credentialsId: 'github-log'
                }
            }
        }

        stage('Maven Compile') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    sh 'mvn compile'
                }
            }
        }

        stage('Mockito Test') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    echo 'Running tests...'
                    sh 'mvn test'
                }
            }
        }

        stage('SonarQube / Jacoco') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                    }
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    echo 'Deploying to Nexus...'
                    sh 'mvn deploy -Dnexus.login=admin -Dnexus.password=nexus'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    sh 'docker build -t $DOCKER_IMAGE .'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    script {
                        docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                            sh 'docker push $DOCKER_IMAGE'
                        }
                    }
                }
            }
        }

        stage('Docker Compose with Monitoring') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    echo 'Starting application and monitoring services with Docker Compose...'
                    sh 'docker compose -f docker-compose.yml up -d'
                }
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
                def failedStage = currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause)?.getShortDescription()
                def message = """
                {
                    "text": "❌ *Deployment Failed!* :x:",
                    "attachments": [
                        {
                            "color": "#ff0000",
                            "text": "Something went wrong during the deployment process in stage: ${failedStage}. Please check the Jenkins console output for more details. :warning:\n*Summary:*\n- Docker Image: $DOCKER_IMAGE\n- Branch: fedichebbi\n\n*Immediate action required!*"
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
