pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'Docker-credentials'
        DOCKER_IMAGE = 'faroukdaadaa/instructor_devops'
    }

    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling...'
                git branch: 'Farouk',
                    url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git',
                    credentialsId: 'DevOps'
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
                withSonarQubeEnv('sonar') {
                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                sh 'mvn deploy -Dnexus.login=admin -Dnexus.password=Nexus'
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

        stage('Docker Compose') {
            steps {
                echo 'Starting application and monitoring services with Docker Compose...'
                sh 'docker compose -f docker-compose.yml up -d'
            }
        }
    }


}