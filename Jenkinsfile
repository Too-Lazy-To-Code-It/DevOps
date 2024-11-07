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
                sh 'mvn compile'
            }
        }

        /*stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                script {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn clean verify sonar:sonar'
                    }
                }
            }
        }*/

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                sh 'mvn deploy -Dnexus.login=admin -Dnexus.password=nexus'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                script {
                    // Build the Docker image using the provided Dockerfile
                    sh "docker build -t ${DOCKER_IMAGE} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                echo 'Pushing Docker image to Docker Hub...'
                script {
                    // Push the Docker image to Docker Hub
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        sh "docker push ${DOCKER_IMAGE}"
                    }
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
