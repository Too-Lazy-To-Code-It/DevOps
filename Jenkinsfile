pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'ahmedamirbouteraa', credentialsId: 'gitauth', url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git'
            }
        }
        stage('Build') {
            steps {
                // Your build steps here
            }
        }
        stage('Test') {
            steps {
                // Your test steps here
            }
        }
        stage('Deploy') {
            steps {
                // Your deploy steps here
            }
        }
    }
}
