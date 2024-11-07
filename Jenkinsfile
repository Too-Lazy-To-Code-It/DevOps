pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'snowyxd/devops_gamix'  // Docker image name
        DOCKER_CREDENTIALS_ID = 'Docker-credentials'  // Jenkins credentials ID for Docker Hub
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'ahmedamirbouteraa', credentialsId: 'gitauth', url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git'
            }
        }

        stage('Maven Clean and Compile') {
            steps {
                echo 'Running mvn clean compile...'
                sh 'mvn clean compile'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image for Spring Boot application...'
                script {
                    // Build the Spring Boot Docker image
                    sh 'docker build -t $DOCKER_IMAGE .'
                }
            }
        }

        stage('Docker Compose Up') {
            steps {
                echo 'Starting Docker Compose with Spring Boot and MySQL...'
                script {
                    // Run Docker Compose to start both Spring Boot app and MySQL container
                    sh 'docker-compose -f docker-compose.yml up -d'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                script {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn clean verify sonar:sonar'
                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    echo 'Pushing Docker image to Docker Hub...'
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        sh 'docker push $DOCKER_IMAGE'
                    }
                }
            }
        }

        stage('Tear Down Docker Compose') {
            steps {
                echo 'Tearing down Docker Compose environment...'
                script {
                    // Shut down the Docker Compose services
                    sh 'docker-compose down'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Please check logs for errors.'
        }
    }
}
