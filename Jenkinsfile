pipeline {
    agent any
    environment {
            DOCKER_NAME = 'DOCKER'
            DOCKER_REPO = '2lazy2nameit/devops'
        }
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
        stage('Run Test with mockito') {
                    steps {
                        sh 'mvn test'
                    }
                }
        stage('SonarQube Quality') {
                       steps {
                                withSonarQubeEnv('sonarqube') {
                                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                                }
                            }
                        }
        stage('Nexus Deploying') {
                    steps {
                        echo 'Deploying Nexus.....Be Patient'
                        sh 'mvn deploy -Dnexus.login=admin -Dnexus.password=1'
                    }
                }
    }
}