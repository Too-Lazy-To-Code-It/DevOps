pipeline {
    agent any
    environment {
            DOCKER_NAME = 'DOCKER'
            DOCKER_REPO = '2lazy2nameit/devops'
                     SCANNER_HOME = tool 'sonar-scanner'
                       // This can be nexus3 or nexus2
                    NEXUS_VERSION = "nexus3"
                    // This can be http or https
                    NEXUS_PROTOCOL = "http"
                    // Where your Nexus is running
                    NEXUS_URL = "192.168.223.128:8081"
                    // Repository where we will upload the artifact
                    NEXUS_REPOSITORY = "maven-releases"
                    // Jenkins credential id to authenticate to Nexus OSS
                    NEXUS_CREDENTIAL_ID = "nexusCredential"
                    ARTIFACT_VERSION = "1.0"
        }
    stages {
            stage("Clean Up") {
                steps {
                    deleteDir() // Clean the workspace
                }
            }

        stage('Checkout GIT') {
            steps {
                echo 'Pulling...'
                git branch: 'Adam',
                    url: 'https://github.com/Too-Lazy-To-Code-It/DevOps.git',
                    credentialsId: 'DevOps'
            }
        }
        stage('OWASP Scan') {
                    steps {
                        echo "Cleaning up OWASP Dependency Check database files..."

                        // Delete the old database files if they exist
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

                        // Run the OWASP Dependency Check against the project
                        dependencyCheck additionalArguments: '', odcInstallation: 'owasp-m'

                        // Publish the Dependency Check report
                        dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
                    }
                }
        stage("Maven clean") {
            steps {
                script {
                echo "mvn cleaning...waiting"
                    sh 'mvn clean'
                }
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
                        stage('mvn package') {
                            steps {
                                sh 'mvn package -DskipTests'
                            }
                        }

                                stage('JaCoCo Report') {
                                    steps {
                                        // Generate the JaCoCo report
                                        sh 'mvn jacoco:report'
                                    }
                                }
                                        stage('Copy JaCoCo Report') {
                                            steps {

                                                // Copy the JaCoCo report to the Jenkins workspace
                                                sh 'cp -R target/site/jacoco/index.html .'
                                            }
                                        }
                                                        stage("publish to nexus") {
                                                    steps {
                                                        script {
                                                            // Read POM xml file using 'readMavenPom' step , this step 'readMavenPom' is included in: https://plugins.jenkins.io/pipeline-utility-steps
                                                            pom = readMavenPom file: "pom.xml";
                                                            // Find built artifact under target folder
                                                            filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                                                            // Print some info from the artifact found
                                                            echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                                                            // Extract the path from the File found
                                                            artifactPath = filesByGlob[0].path;
                                                            // Assign to a boolean response verifying If the artifact name exists
                                                            artifactExists = fileExists artifactPath;

                                                            if(artifactExists) {
                                                                echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";

                                                                nexusArtifactUploader(
                                                                    nexusVersion: NEXUS_VERSION,
                                                                    protocol: NEXUS_PROTOCOL,
                                                                    nexusUrl: NEXUS_URL,
                                                                    groupId: pom.groupId,
                                                                    version: ARTIFACT_VERSION,
                                                                    repository: NEXUS_REPOSITORY,
                                                                    credentialsId: NEXUS_CREDENTIAL_ID,
                                                                    artifacts: [
                                                                        // Artifact generated such as .jar, .ear and .war files.
                                                                        [artifactId: pom.artifactId,
                                                                        classifier: '',
                                                                        file: artifactPath,
                                                                        type: pom.packaging]
                                                                    ]
                                                                );

                                                            } else {
                                                                error "*** File: ${artifactPath}, could not be found";
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
                                               -Dsonar.projectKey=DevopsJenkins '''   }

                                    }
                                }


        stage('Docker Image Building') {
                    steps {

                         sh 'docker build -t $DOCKER_REPO .'
                            }
                        }
                                                                    /*    stage('Trivy gaming') {
                                                                            steps {
                                                                                echo "Scanning Docker image with Trivy..."
                                                                                sh 'trivy image --timeout 20m --format table --scanners vuln --debug --ignore-unfixed -o trivy-imageesprit-report.html 2lazy2nameit/devops'
                                                                            }
                                                                        }*/
        stage('Docker Image push') {
                steps {
                script {
                docker.withRegistry('https://index.docker.io/v1/', DOCKER_NAME) {
                                                        sh 'docker push 2lazy2nameit/devops'
                                                    }
                                                }
                                            }
                                        }



                                 stage('Docker Compose ') {
                                            steps {
                                                echo 'Application on starting and monitoring dcoker composing..'
                                                sh 'docker compose -f docker-compose.yml up -d'
                                            }
                                        }


    }
  /*  post {
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

            emailext (
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
}*/
}