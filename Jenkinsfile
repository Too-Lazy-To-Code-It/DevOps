pipeline {
    agent any

    environment {
            DOCKER_CREDENTIALS_ID = 'docker-hub-credentials' // ID you set in the previous step
            DOCKER_IMAGE = 'mohamedrayen/sky_devops' // Replace with your image name
        }


    stages {

        stage('Initilize Containers') {
            steps {
                sh 'docker start nexus sonarqube prometheus'
            }
        }

        stage('Clean') {
            steps {
                sh 'mvn clean'
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
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                   sh 'mvn sonar:sonar'
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
                        script {

                            sh 'mvn clean deploy -DskipTests'
                        }
                    }
                }
        stage('Test and Coverage') {
                    steps {
                        sh 'mvn test jacoco:report'
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
                            // Log in to Docker Hub
                            docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                                // Push the Docker image
                                sh 'docker push $DOCKER_IMAGE'
                            }
                        }
                    }
                }
        stage('docker compose') {
                  steps {
                         sh 'docker compose up -d '
                 }
        }
    }
}
