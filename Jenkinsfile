pipeline {
    agent any

    environment {
        SONAR_URL = "http://192.168.5.1:9000/"
        SONAR_LOGIN = "sqa_272bebdb05ad6099336b79cb658466ff98e9a626"  // Or you can store this securely as a Jenkins credential
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from Git
                git branch: 'ahmedamirbouteraa', credentialsId: 'gitauth', url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git'
            }
        }

        stage('Maven Clean') {
            steps {
                echo 'Running mvn clean...'
                sh 'mvn clean'
            }
        }

        stage('Maven Compile') {
            steps {
                echo 'Running mvn compile...'
                sh 'mvn compile'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                script {
                    // Ensure SonarQube environment is configured in Jenkins
                    withSonarQubeEnv('sonarqube') {
                        // Run SonarQube analysis using Maven
                        sh "mvn sonar:sonar -Dsonar.projectKey=my_project_key -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_LOGIN} -x"
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying...'
                // Add your actual deploy command here
            }
        }
    }

    post {
        success {
            echo 'SonarQube analysis completed successfully.'
        }
        failure {
            echo 'SonarQube analysis failed.'
        }
    }
}
