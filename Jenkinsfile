pipeline {
    agent {
        label 'dev'
    }
    tools {
        maven 'mvn3'
        jdk 'jdk17'
        // docker 'docker24'            // we do not have to mention this in tools otherwise we will get error
    }
    environment {
        // SCANNER_HOME = tool 'SonarScanner'        // we define this tool and we can use it below.
        DOCKERHUB_USERNAME = "devopstech24"
        APPLICATION_NAME = "spring-first-app-jenkins-ci-pipeline"
        IMAGE_NAME = "${DOCKERHUB_USERNAME}" + "/" + "${APPLICATION_NAME}"

        RELEASE = "1.0.0"
        IMAGE_TAG = "${RELEASE}" + "-" + "${env.BUILD_NUMBER}"

    }
    stages {
        stage ('Workspace Cleanup') {
            steps {
                cleanWs()
            }  
        }

        stage ('Git Checkout') {
            steps {
                sh 'mvn --version'
                sh 'java --version'
                sh 'docker --version'
                git branch: 'main', credentialsId: 'git-cred', url: 'https://github.com/azure123devops123/spring-first-app'
                sh 'ls'
            }  
        }

        stage ('Code Compile') {
            steps {
                sh 'mvn clean compile'      // code compile 
            }
        }

        stage ('Code Unit Test') {
            steps {
                sh 'mvn test -DskipTests=True'  // skip tests but not in production :)
            }
        }

        stage ('SonarQube Code Analysis') {
            steps {
                withSonarQubeEnv('SonarScanner') {      // Pass only server name as an argument
                    sh 'mvn clean verify sonar:sonar'   // Analyzing a Maven project consists of running a Maven goal: sonar:sonar from the directory that holds the main project pom.xml
                }
            }
        }

        stage ('OWASP Dependencies Check') {   // It will take 5 to 10 minutes when we run for the first time because it will download the National Vulnerability Database (NVD) from 2002 to onwards
            steps {
                // dependencyCheck additionalArguments: '--scan ./', odcInstallation: 'DC'          // WE CAN USE THIS ONE LINER CODE AND IT WILL WORK FINE BUT BELOW CODE IS GENRATED BY Snippet Generator.
                dependencyCheck additionalArguments: '''--project	spring-first-app-jenkins-ci-pipeline
                --scan	./
                --format	XML''', odcInstallation: 'DC'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }

        stage ('Build Docker Image') {
            steps {
                script {                // Groovy Script for Building Docker Image
                    withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker24') {
                       dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                    }
                }
            }
        }

        stage ('Trivy Scaning Docker Image') {
            steps {
                // Install Trivy using Install Script - > https://aquasecurity.github.io/trivy/v0.18.3/installation/#install-script
                sh 'curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin v0.18.3'
                sh 'trivy image ${IMAGE_NAME}:${IMAGE_TAG} > trivy-report.xml'
            }
        }

        stage ('Push to Docker Image Registry') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker24') {
                       dockerImage.push()
                       dockerImage.push('latest')
                    }
                }
            }
        }

        stage ('Docker Scout Image Scan') {
            steps {
                script {
                     // Install Docker Scout
                    sh 'curl -sSfL https://raw.githubusercontent.com/docker/scout-cli/main/install.sh | sh -s -- -b /usr/local/bin'
                    
                    withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker24') {
                       // Analyze and fail on critical or high vulnerabilities
                       sh 'docker-scout cves ${IMAGE_NAME}:${IMAGE_TAG} --exit-code --only-severity critical'
                    }
                }
            }
        }

    }

}