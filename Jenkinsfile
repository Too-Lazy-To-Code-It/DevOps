/*pipeline {
    agent any

    stages {
        stage('Main') {
            steps {
                // Compile the Spring Boot project
                echo "Echo Test of Rayen Branch"
            }
        }

        stage('Build') {
            steps {
                // Check out the code from the repository
                checkout scm

                // Run Maven clean install
                sh 'mvn clean package'
            }
        }


    }
}*/


pipeline {
    agent any

    stages {
        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }
        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                   sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Docker Build') {
                    steps {
                        script {
                            // Build the Docker image
                            sh 'docker build --no-cache -t my-spring-boot-app .'
                        }
                    }
                }
                stage('Docker Run') {
                    steps {
                        script {
                            // Run the Docker container on port 8081
                            sh 'docker run -d -p 8081:8080 --name my-spring-boot-app my-spring-boot-app'
                        }
                    }
                }

    }
}
