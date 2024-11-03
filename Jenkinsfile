pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
        DOCKER_IMAGE = 'mohamedrayen/sky_devops'
    }

    stages {

        stage('Initialize Containers') {
            steps {
                sh 'docker start nexus sonarqube prometheus'
            }
        }

        stage('Clean') {
            steps {
                sh 'mvn clean jacoco:prepare-agent'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Test Class Subscription') {
            steps {
                sh 'mvn test'
            }
        }
        /*stage('Test and Coverage') {
            steps {
                sh 'mvn test jacoco:report'
            }
        }*/

        stage('SonarQube Analysis and Jacoco') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                sh 'mvn clean deploy -DskipTests'
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
                sh 'docker compose up -d'
            }
        }
    }
}
