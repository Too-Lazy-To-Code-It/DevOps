pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'snowyxd/Devops_gamix'  // Docker image name (change this if needed)
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
                script {
                    echo 'Building Docker image...'
                    sh 'docker build -t $DOCKER_IMAGE .'
                }
            }
        }

        stage('Login to Docker Hub') {
            steps {
                script {
                    echo 'Logging in to Docker Hub...'
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    echo 'Pushing Docker image to Docker Hub...'
                    sh 'docker push $DOCKER_IMAGE'
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
