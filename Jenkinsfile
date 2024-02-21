def COLOR_MAP = ['SUCCESS': 'good', 'FAILURE': 'danger', 'UNSTABLE': 'danger', 'ABORTED': 'danger']

pipeline {
    agent {
        label 'dev'
    }
    // triggers {
    //     cron('2 3 * * *')
    //     // cron('H 4/* 0 0 1-5')
    // }
    tools {
        maven 'mvn3'
        jdk 'jdk17'
        // docker 'docker24'            // we do not have to mention this in tools otherwise we will get error
    }
    environment {
        // SCANNER_HOME = tool 'SonarScanner'        // we define this tool and we can use it below.
        DOCKERHUB_USERNAME = "devopstech24"
        APPLICATION_NAME = "java-spring-app-ci-pipeline"
        IMAGE_NAME = "${DOCKERHUB_USERNAME}" + "/" + "${APPLICATION_NAME}"

        RELEASE = "1.0.0"
        IMAGE_TAG = "${RELEASE}" + "-" + "${env.BUILD_NUMBER}"

        JENKINS_API_TOKEN = credentials("JENKINS_API_TOKEN")            // make sure it must be correct

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
                git branch: 'main', credentialsId: 'github-cred', url: 'https://github.com/azure123devops123/spring-first-app'
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

        stage("Quality Gate") {
            steps {
                script {
                    waitForQualityGate abortPipeline: false, credentialsId: 'sonarqube-token'
                }
            }
        }

        stage ('OWASP Dependencies Check') {   // It will take 5 to 10 minutes when we run for the first time because it will download the National Vulnerability Database (NVD) from 2002 to onwards
            steps {
                // dependencyCheck additionalArguments: '--scan ./', odcInstallation: 'DC'          // WE CAN USE THIS ONE LINER CODE AND IT WILL WORK FINE BUT BELOW CODE IS GENRATED BY Snippet Generator.
                dependencyCheck additionalArguments: '''--project	java-spring-app-ci-pipeline
                --scan	./
                --format	XML''', odcInstallation: 'DC'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }

        stage ('Build Docker Image') {
            steps {
                script {                // Groovy Script for Building Docker Image
                    withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker24') {
                       // dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                       dockerImage = sh 'docker buildx build --platform linux/amd64,linux/arm64 -t ${IMAGE_NAME}:${IMAGE_TAG} .'
                    }
                }
            }
        }

        stage ('Analyze Image using Trivy Image Scanner') {
            steps {
                // Install Trivy using Install Script: - > https://aquasecurity.github.io/trivy/v0.18.3/installation/#install-script
                sh 'curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin v0.18.3'
                sh 'trivy image ${IMAGE_NAME}:${IMAGE_TAG} > trivy-report.xml'
            }
        }

        stage ('Push to Docker Image Registry') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker24') {
                        sh 'docker push ${IMAGE_NAME}:${IMAGE_TAG} --platform linux/amd64,linux/arm64'
                    //    dockerImage.push()
                    //    dockerImage.push('latest')
                    }
                }
            }
        }

        stage ('Analyze Image using Docker Scout Image Scanner') {
            steps {
              // Install Docker Scout using curl - Note make sure its inside the steps and before the Groovy script with the correct alignment.
              sh 'curl -sSfL https://raw.githubusercontent.com/docker/scout-cli/main/install.sh | sh -s -- -b /usr/local/bin'

                script {
                    withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker24') {
                       // Analyze and fail on critical or high vulnerabilities
                       sh 'docker-scout cves ${IMAGE_NAME}:${IMAGE_TAG} --exit-code --only-severity critical'
                    }
                }
            }
        }

        stage ('Trigger Continuous Delivery Pipeline') {
            steps {
                script {
                    sh "curl -v -k --user admin:${JENKINS_API_TOKEN} -X POST -H 'cache-control: no-cache' -H 'content-type: application/x-www-form-urlencoded' --data 'IMAGE_TAG=${IMAGE_TAG}' 'https://jenkins.dev.devopstech24.click/job/gitops-cd-pipeline/buildWithParameters?token=gitops-token'"
                }
            }
        }

        // //++++++++++++++++ DOCKER CONTAINER DEPLOYMENT WITH WORKSPACE CLEANUPS ++++++++++++++++\\
        // stage ('Docker Container Deployment') {
        //     steps {
        //         script {
        //             sh "docker run -d -p 8085:8080 ${IMAGE_NAME}:${IMAGE_TAG}"
        //         }
        //     }
        // }

        // stage('Cleanup Artifacts') {
        //     steps {
        //       script {
        //         //sh 'docker rmi ${IMAGE_NAME}:${IMAGE_TAG}'    // Remove Current Tagged Image
        //         sh 'docker rmi ${IMAGE_NAME}:latest'          // Remove latest Tagged Image
        //         sh 'docker image prune --all --force'         // Remove all dangling images without prompt for confirmation
        //       }
        //     }
        // }

        stage('Cleanup Artifacts') {
            steps {
              script {
                //sh 'docker rmi ${IMAGE_NAME}:${IMAGE_TAG}'    // Remove Current Tagged Image
                sh 'docker rmi ${IMAGE_NAME}:latest'          // Remove latest Tagged Image
                sh 'docker image prune --all --force'         // Remove all dangling images without prompt for confirmation
              }
            }
        }

    } // end of stages section
    post {
        always {
            echo 'Slack Notifications'
            slackSend (
                channel: '#spring-app',   // change your channel name
                color: COLOR_MAP[currentBuild.currentResult],
                message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} \n build ${env.BUILD_NUMBER} \n More info at: ${env.BUILD_URL}"
            )
        }
    }
} 