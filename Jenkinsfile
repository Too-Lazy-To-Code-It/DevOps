pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'Docker-credentials' // Docker credentials ID
        DOCKER_IMAGE = 'fedichebbi/course_devops' // Docker image name
    }

    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling...'
                git branch: 'fedichebbi',
                    url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git',
                    credentialsId: 'github-log'
            }
        }

        stage('Maven Compile') {
            steps {
                sh 'mvn compile'
            }
        }
        
        stage('Mockito Test') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
            }
        }
        
        stage('SonarQube / Jacoco') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
            }
        }
}


        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus...'
                sh 'mvn deploy -Dnexus.login=admin -Dnexus.password=nexus'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE .'
            }
        }

        stage('Push Docker Image') {
                    steps {
                        script {
                            docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                                sh 'docker push $DOCKER_IMAGE'
                            }
                        }
                    }
                }
        
        stage('docker compose') {
            steps {
                sh 'docker compose up -d '
            }
        }
    }
}
