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
           stage('SonarQube / Jacoco') {
                  steps {
                      script {
                          // Running SonarQube analysis with JaCoCo coverage report
                          currentStage = 'SonarQube / Jacoco'
                      }
                      withSonarQubeEnv(SONARQUBE_SERVER) {
                          // Run the SonarQube analysis and pass JaCoCo coverage XML file
                          sh 'mvn clean verify sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
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
