pipeline {
    agent any
    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'ahmedamirbouteraa', url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git'
            }
        }
        stage('Build') {
            steps {
                echo 'Building...'
                // Add build steps here
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests...'
                // Add test steps here
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
                // Add deployment steps here
            }
        }
    }
}
