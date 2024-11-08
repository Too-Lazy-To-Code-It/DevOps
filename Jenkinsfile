pipeline {
    environment {
        ANGULAR_REPO = 'https://github.com/Too-Lazy-To-Code-It/Devops-Front.git'
        DOCKER_REPO_ANGULAR = '2lazy2nameit/angulardevops'
        DOCKER_NAME = 'DOCKER'
        DOCKER_REPO = '2lazy2nameit/devops'
        SCANNER_HOME = tool 'sonar-scanner'
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.223.128:8081"
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_CREDENTIAL_ID = "nexusCredential"
        ARTIFACT_VERSION = "1.0"
    }
    agent any
    triggers {
        githubPush()
    }
    stages {
        stage("Clean Up") {
            steps {
                deleteDir()
            }
        }
        stage('Checkout GIT') {
            steps {
                withCredentials([string(credentialsId: 'github-adam', variable: 'GITHUB_TOKEN')]) {
                    sh '''
                        git clone --branch Adam https://Too-Lazy-To-Code-It:${GITHUB_TOKEN}@github.com/Too-Lazy-To-Code-It/DevOps.git
                    '''
                }
            }
        }
        stage('Checkout Angular') {
            steps {
                git url: "${ANGULAR_REPO}", branch: 'main'
            }
        }
        stage('OWASP Scan') {
            steps {
                echo "Cleaning up OWASP Dependency Check database files..."
                sh '''
                    if [ -f /data/jenkins/nvid/dc.h2.db ]; then
                        rm /data/jenkins/nvid/dc.h2.db
                    fi
                    if [ -f /data/jenkins/nvid/dc.lock.db ]; then
                        rm /data/jenkins/nvid/dc.lock.db
                    fi
                    if [ -f /data/jenkins/nvid/dc.trace.db ]; then
                        rm /data/jenkins/nvid/dc.trace.db
                    fi
                '''
                echo "Running OWASP Dependency Check..."
                dependencyCheck additionalArguments: '', odcInstallation: 'owasp-m'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
        stage("Move Project Files") {
            steps {
                sh '''
                    mv DevOps/* . || true
                '''
            }
        }
        stage("Maven clean") {
            steps {
                sh '''
                    ls -l /var/lib/jenkins/workspace/DevOps &&
                    mvn clean
                '''
            }
        }
        stage('Compile Artifact') {
            steps {
                echo "Compiling..."
                sh 'mvn compile'
            }
        }
        stage('Run Tests mockito') {
            steps {
                sh 'mvn test'
            }
        }
        stage('mvn package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
        stage('JaCoCo Report') {
            steps {
                sh 'mvn jacoco:report'
            }
        }
        stage('Copy JaCoCo Report') {
            steps {
                sh 'cp -R target/site/jacoco/index.html .'
            }
        }
        stage("Publish to Nexus") {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml"
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}")
                    artifactPath = filesByGlob[0].path
                    artifactExists = fileExists artifactPath

                    if(artifactExists) {
                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: ARTIFACT_VERSION,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId, classifier: '', file: artifactPath, type: pom.packaging]
                            ]
                        )
                    } else {
                        error "*** File: ${artifactPath}, could not be found"
                    }
                }
            }
        }
        stage('SonarQube Quality') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }
        stage('SonarQube Analysis(SAST)') {
            steps {
                withCredentials([ string(credentialsId: 'Sonarqube-url', variable: 'SONARQUBE_HOST_URL'),
                                  string(credentialsId: 'sonarqube', variable: 'SONARQUBE_LOGIN') ]) {
                    sh '''$SCANNER_HOME/bin/sonar-scanner \
                        -Dsonar.host.url=${SONARQUBE_HOST_URL} \
                        -Dsonar.token=${SONARQUBE_LOGIN} \
                        -Dsonar.projectName=Devops \
                        -Dsonar.java.binaries=. \
                        -Dsonar.projectKey=DevopsJenkins'''
                }
            }
        }
        stage('Docker Image Building') {
            steps {
                sh 'docker build -t $DOCKER_REPO:$BUILD_NUMBER .'
            }
        }
        stage('Docker Image Scan') {
            steps {
                echo "Scanning Docker image with Trivy..."
                sh "trivy clean --java-db"
                sh "trivy image --format table --scanners vuln --debug --ignore-unfixed -o trivy-imageesprit-report.html $DOCKER_REPO"
            }
        }
stage('Docker Image Push') {
    steps {
        script {
            docker.withRegistry('https://index.docker.io/v1/', DOCKER_NAME) {
                sh 'docker push $DOCKER_REPO:$BUILD_NUMBER'
            }
        }
    }
}
stage('Push Angular Docker Image') {
    steps {
        script {
            docker.withRegistry('https://index.docker.io/v1/', DOCKER_NAME) {
                sh 'docker push $DOCKER_REPO_ANGULAR:$BUILD_NUMBER'
            }
        }
    }

        stage('Docker Compose') {
            steps {
                sh 'docker compose -f docker-compose.yml up -d'
            }
        }
    }
    post {
        always {
            script {
                def jobName = env.JOB_NAME
                def buildNumber = env.BUILD_NUMBER
                def pipelineStatus = currentBuild.result ?: 'UNKNOWN'
                def bannerColor = pipelineStatus.toUpperCase() == 'SUCCESS' ? 'green' : 'red'

                def body = """
                <html>
                    <body>
                        <div style="border: 4px solid ${bannerColor}; padding: 10px;">
                            <h2>${jobName} - Build ${buildNumber}</h2>
                            <div style="background-color: ${bannerColor}; padding: 10px;">
                                <h3 style="color: white;">Pipeline Status: ${pipelineStatus.toUpperCase()}</h3>
                            </div>
                        </div>
                    </body>
                </html>
                """

                emailext(
                    subject: "${jobName} - Build ${buildNumber} - ${pipelineStatus.toUpperCase()}",
                    body: body,
                    to: 'adam.rafraf@esprit.tn',
                    from: 'jenkins@example.com',
                    replyTo: 'jenkins@example.com',
                    mimeType: 'text/html',
                    attachmentsPattern: 'trivy-imageesprit-report.html,index.html,dependency-check-report.xml'
                )
            }
        }
    }
}
