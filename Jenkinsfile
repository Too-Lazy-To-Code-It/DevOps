pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'Docker-credentials'
        DOCKER_IMAGE = 'faroukdaadaa/instructor_devops'
    }

    stages {
        stage('GIT') {
            steps {
                echo 'Pulling...'
                git branch: 'Farouk',
                    url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git',
                    credentialsId: 'DevOps'
            }
        }

        stage('Maven Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Test Unitaire Mockito') {
            steps {
                echo 'Starting tests...'
                sh 'mvn test'
            }
        }

        stage('SonarQube with coverage') {
            steps {
                withSonarQubeEnv('sonar') {
                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }

        stage('Nexus Deploying') {
            steps {
                echo 'Deploying to Nexus...'
                sh 'mvn deploy -Dnexus.login=admin -Dnexus.password=Nexus'
            }
        }

        stage('Docker Building Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE .'
            }
        }

        stage('Docker Pushing Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        sh 'docker push $DOCKER_IMAGE'
                    }
                }
            }
        }

        stage('Docker Up') {
            steps {
                echo 'Starting application with Docker Compose...'
                sh 'docker compose -f docker-compose.yml up -d'
            }
        }
    }


}