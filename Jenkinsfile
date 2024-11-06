pipeline {
    agent any
    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling...'
                git branch: 'Adam',
                    url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git',
                    credentialsId: 'DevOps'
            }
        }

        stage('Maven Compile') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('Run Test with mockio') {
                    steps {
                        sh 'mvn test'
                    }
                }

    }
}