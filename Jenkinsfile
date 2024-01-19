// https://www.yamllint.com/        validate yaml

pipeline {
    agent any
    tools {
      maven 'maven3'
      jdk 'jdk17'
      //docker 'docker24'
    }
    environment {
      SCANNER_HOME= tool 'sonar-scanner'        // we define this tool and we can use it below.

      DOCKER_USER = "devopstech24"
      APP_NAME = "java-application-jenkins-ci-pipeline" // This repo will be created on dockrhub automatically.
      IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"

      RELEASE = "1.0.0" // Semantic versioning (Major.Minor.Patch)
      IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"          // BUILD_NUMBER is the environment variable
    }
    
    stages {
        stage('Cleanup Workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Git Checkout') {
            steps {
                git branch: 'main' , url: 'https://github.com/azure123devops123/spring-first-app'
                }
        }
        stage('Code Compile') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('Unit Test') {
            steps {
                sh 'mvn test -DskipTests=true'
            }
        }
        // stage('BuSonarQube Static Code Analysis') {
        //     steps {
        //         withSonarQubeEnv('sonar') {         // Pass only server name
        //             //sh "${scannerHome}/bin/sonar-scanner"
        //            // Following -Dsonar mean argument which we pass and ''' means its multilines but single block code
        //            sh ''' ${SCANNER_HOME}/bin/sonar-scanner -Dsonar.projectKey=spring-first-app -Dsonar.projectName=spring-first-app \
        //            -Dsonar.java.binaries-. '''
        //         }
        //     }
        // }
        stage('OWASP Dependency Check') {
            steps {
                dependencyCheck additionalArguments: ' --scan ./', odcInstallation: 'DC'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests=true'
            }
        }
        // stage('Deploy Artifact to Nexus') {
        //     steps {
        //       withMaven(globalMavenSettingsConfig: 'global-maven', jdk: 'jdk17', maven: 'maven3', mavenSettingsConfig: '', traceability: true) {
        //           sh 'mvn deploy -DskipTests=true'
        //       }
        //     }
        // }
        stage('Deploy Artifact to Nexus') {
            steps {
              withMaven(globalMavenSettingsConfig: 'global-maven', jdk: 'jdk17', maven: 'maven3', mavenSettingsConfig: '', traceability: true) {
                  sh 'mvn deploy -DskipTests=true'
              }
            }
        }
        stage ('Build & Push Docker Image to Docker Hub') {
            steps {
              // Install Docker inside Container
              sh 'curl -fsSLO https://download.docker.com/linux/static/stable/x86_64/docker-24.0.7.tgz && tar --strip-components=1 -xvzf docker-24.0.7.tgz -C /usr/local/bin'   // YOU CAN FIND CURRENT BINAARY VERSION THEN DOWNLOAD AND INSTALL BELOW: https://download.docker.com/linux/static/stable/x86_64/   => docker-24.0.7.tgz 
              script {
                // Build and Tag Image
                docker.withRegistry('','dockerhub-cred') {
                    dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                }
                // Push Image to Dockerhub
                docker.withRegistry('','dockerhub-cred') {
                    dockerImage.push();
                    dockerImage.push('latest')   // We can't push without Jenkins having Docker Hub Credentials (DockerID and Token (note:-Password will not work).
                }
              }
            }
          }
        stage ('Analyze Image - Docker Scout Image Scanner') {
            steps {
              // Install Docker Scout inside Container
              sh 'curl -sSfL https://raw.githubusercontent.com/docker/scout-cli/main/install.sh | sh -s -- -b /usr/local/bin'
              script {
                docker.withRegistry('','dockerhub-cred') {
                  sh 'docker-scout cves ${IMAGE_NAME}:${IMAGE_TAG} --exit-code --only-severity critical'
                }
              }
            }
       }
    }
}