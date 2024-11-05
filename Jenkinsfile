pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                // Checkout code from Git
                git branch: 'ahmedamirbouteraa', credentialsId: 'gitauth', url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git'
            }
        }
        stage('Build') {
            steps {
                // Example build command
                sh 'echo Building...'
                // Add your actual build command here, for example:
                // sh './gradlew build' or 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                // Example test command
                sh 'echo Running tests...'
                // Add your actual test command here, for example:
                // sh './gradlew test' or 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                // Example deploy command
                sh 'echo Deploying...'
                // Add your actual deploy command here
            }
        }
    }
}
