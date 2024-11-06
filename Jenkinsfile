pipeline {
    agent any

    environment {
        SONAR_URL = "http://192.168.1.5:9000/"  // Replace with your SonarQube URL
        SONAR_LOGIN = "sqa_272bebdb05ad6099336b79cb658466ff98e9a626"  // Replace with your SonarQube token or store it securely in Jenkins credentials
        NEXUS_URL = "http://192.168.1.5:8081/repository/maven-releases/"  // Replace with your Nexus URL
        NEXUS_CREDENTIALS_ID = "deploymentRepo"  // Jenkins credentials ID for Nexus
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from Git
                git branch: 'ahmedamirbouteraa', credentialsId: 'gitauth', url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git'
            }
        }

        stage('Maven Clean and Compile') {
            steps {
                echo 'Running mvn clean compile...'
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running mvn test...'
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                script {
                    // Ensure SonarQube environment is configured in Jenkins
                    withSonarQubeEnv('sonarqube') {
                        // Run SonarQube analysis using Maven
                        sh "mvn sonar:sonar -Dsonar.projectKey=my_project_key -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_LOGIN}"
                    }
                }
            }
        }

        stage('Maven Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus using Maven deploy...'
                withCredentials([usernamePassword(credentialsId: NEXUS_CREDENTIALS_ID, passwordVariable: 'NEXUS_PASSWORD', usernameVariable: 'NEXUS_USERNAME')]) {
                    // Run the Maven deploy
                    sh 'mvn deploy'
                }
            }
        }
    }

    post {
        success {
            echo 'SonarQube analysis and Nexus deployment completed successfully.'
        }
        failure {
            echo 'SonarQube analysis or Nexus deployment failed.'
        }
    }
}
