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


        stage('JaCoCo Test Coverage and SonarQube Analysis') {
            steps {
                echo 'Running JaCoCo for code coverage and SonarQube analysis...'
                script {
                    // Run SonarQube analysis and JaCoCo test coverage
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn clean verify sonar:sonar'
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
