// // Simple PIPELINE   - MY WORKING CODE
// pipeline {
//     agent any
//     stages {
//         stage ('Demo') {
//             steps {
//                 echo 'Hello World Demonstration - YAHOO'
//             }
//         }
//     }
// }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// // Continuous Integration PIPELINE - abhishek NOT WORKING CODE
// pipeline {
//   agent {
//     docker {
//       image 'abhishekf5/maven-abhishek-docker-agent:v1'
//       args '--user root -v /var/run/docker.sock:/var/run/docker.sock' // mount Docker socket to access the host's Docker daemon
//     }
//   }
//   stages {
//     stage('Checkout') {
//       steps {
//         sh 'echo passed'
//         git branch: 'main', url: 'https://github.com/azure123devops123/spring-first-app.git'
//       }
//     }
//     stage('Build and Test') {
//       steps {
//         // Build the project and create a JAR file...
//         sh 'mvn -f pom.xml clean package'
//       }
//     }
//     stage('Static Code Analysis') {
//       environment {
//         SONAR_URL = "http://3.104.79.100:9000"
//       }
//       steps {
//         withCredentials([string(credentialsId: 'SonarqubeID', variable: 'SONAR_AUTH_TOKEN')]) {
//           sh 'mvn sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=${SONAR_URL}'
//         }
//       }
//     }
//     stage('Build and Push Docker Image') {
//       environment {
//         DOCKER_IMAGE = "devopstech24/jenkins-spring-first-app:${BUILD_NUMBER}"
//         REGISTRY_CREDENTIALS = credentials('DockerhubID')
//       }
//       steps {
//         script {
//             sh 'docker build -t ${DOCKER_IMAGE} .'
//             def dockerImage = docker.image("${DOCKER_IMAGE}")
//             docker.withRegistry('https://index.docker.io/v1/', "DockerhubID") {
//                 dockerImage.push()
//             }
//         }
//       }
//     }
//   }
// }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Continuous Integration PIPELINE - MY WORKING CODE...

pipeline {

  environment {    // GO INSIDE Manage Jenkins and get the names of both tools we set earlier (myDocker & myMaven)
		// dockerHome = tool 'myDocker'
    // sh 'curl -fsSLO https://download.docker.com/linux/static/stable/x86_64/docker-24.0.7.tgz && tar --strip-components=1 -xvzf docker-24.0.7.tgz -C /usr/local/bin'
    // PATH =  "/usr/local/bin:$PATH"
		// PATH =  "$dockerHome/bin:$PATH"      // add both tools to our path
    
    DOCKER_USER = "devopstech24"
    APP_NAME = "jenkins-devops-microservice"
    IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"

    RELEASE = "1.0.0"
    IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"          // BUILD_NUMBER is the environment variable
	}

  agent {
    docker {
      image 'maven:latest'  //3.9.6
      args '--user root -v /var/run/docker.sock:/var/run/docker.sock' // mount Docker socket to access the host's Docker daemon
    }
  }
  stages {
    stage('Checkout') {
      steps {
        git branch: 'main', credentialsId: 'GithubID', url: 'https://github.com/azure123devops123/spring-first-app.git'
      }
    }
    stage('Build and Test') {
      steps {
        // Build the project and create a JAR file
        // sh 'mvn -f pom.xml clean package'
        sh 'mvn clean package'
      }
    }
    stage('Static Code Analysis') {
      environment {
        SONAR_URL = "http://52.64.145.91:9000"
      }
      steps {
        withCredentials([string(credentialsId: 'SonarqubeID', variable: 'SONAR_AUTH_TOKEN')]) {
          sh 'mvn sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=${SONAR_URL}'
        }
      }
    }
		stage ('Build & Push Docker Image to Docker Hub') {
			steps {
				script {         
					docker.withRegistry('','DockerhubID') {      // First parameter is empty because dockerhub is a default docker registry // second paramter is docker credentials ID that we just created
              // YOU CAN FIND CURRENT BINAARY VERSION THEN DOWNLOAD AND INSTALL BELOW: https://download.docker.com/linux/static/stable/x86_64/   => docker-24.0.7.tgz
              // sh 'curl -fsSLO https://download.docker.com/linux/static/stable/x86_64/docker-24.0.7.tgz && tar --strip-components=1 -xvzf docker-24.0.7.tgz -C /usr/local/bin'
              dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
					} // end of wrapper

          docker.withRegistry('','DockerhubID') {        // First parameter is empty because dockerhub is a default docker registry // second paramter is docker credentials ID that we just created
					    dockerImage.push();
					    dockerImage.push('latest')   // We can't push without Jenkins having Docker Hub Credentials (DockerID and Token (note:-Password will not work).
					} // end of wrapper
				}
			}
		}
    stage ('Analyze Image to Find CVEs using Docker Scout Image Scanner') {
      steps {
        // Install Docker Scout inside Container
        sh 'curl -sSfL https://raw.githubusercontent.com/docker/scout-cli/main/install.sh | sh -s -- -b /usr/local/bin'
        script {
					docker.withRegistry('','DockerhubID') {
            sh 'docker-scout cves ${IMAGE_NAME}:${IMAGE_TAG} --exit-code --only-severity critical'
          }
        }
      }
    }
    stage('Cleanup Artifacts') {
      steps {
        script {
          sh 'docker rmi ${IMAGE_NAME}:${IMAGE_TAG}'  // Remove Current Tag Image
          sh 'docker rmi ${IMAGE_NAME}:latest'  // Remove latest Tag Image
          sh 'docker image prune --force' // Remove dangling images without prompt for confirmation
        }
      }
    }
    stage('Cleanup Workspace'){
      steps {
        cleanWs()
      }
    }
  }
}