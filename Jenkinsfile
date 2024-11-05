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
                // Maven compile command
                echo 'Compiling with Maven...'
                sh 'mvn compile'
            }
        }
        stage('Test') {
            steps {
                // Run tests using Maven
                echo 'Running tests with Maven...'
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                // Example deploy command
                echo 'Deploying...'
                // Add your actual deploy command here
            }
        }
    }
}
