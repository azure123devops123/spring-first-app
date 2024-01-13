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
// Continuous Integration PIPELINE - MY WORKING CODE

pipeline {
  environment {    // GO INSIDE Manage Jenkins and get the names of both tools we set earlier (myDocker & myMaven)
		dockerHome = tool 'myDocker'
		PATH =  "$dockerHome/bin:$PATH"      // add both tools to our path
	}  
  agent {
    docker {
      image 'maven:3.9.6'
      args '--user root -v /var/run/docker.sock:/var/run/docker.sock' // mount Docker socket to access the host's Docker daemon
    }
  }
  stages {
    stage('Checkout') {
      steps {
        sh 'echo passed'
        git branch: 'main', url: 'https://github.com/azure123devops123/spring-first-app.git'
      }
    }
    stage('Build and Test') {
      steps {
        // Build the project and create a JAR file...
        sh 'mvn -f pom.xml clean package'
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
    stage ('Build Docker Image'){
			steps {
				// "docker build -t devopstech24/jenkins-devops-microservice:$env.BUILD_TAG"      // Primitive (OLD) Way
				script {
          //sh 'apt-get update && apt-get install -y apt-utils && apt-get install -y curl'
          sh 'curl https://get.docker.com/ | sh'    // Install Docker using script inside Container
					dockerImage = docker.build("devopstech24/jenkins-devops-microservice:${env.BUILD_TAG}")
				}
			}
		}
		stage ('Push Docker Image to Docker Hub') {
			steps {
				script {         
					// to push the image to docker hub we need to put the wrapper (docker.withRegistry) around below (dockerImage.push)
					docker.withRegistry('','DockerhubID') {        // first parameter is empty because dockerhub is a default docker registry // second paramter is docker credentials ID that we just created
					    dockerImage.push();
					    dockerImage.push('latest');   // We can't push without Jenkins having Docker Hub Credentials (DockerID and Token (note:-Password will not work)
					} // end of wrapper
				}
			}
		}
    // https://github.com/docker/scout-cli
    stage ('Analyze Image to Find CVEs') {
      steps {
        // Install Docker Scout inside Container.
        sh 'curl -sSfL https://raw.githubusercontent.com/docker/scout-cli/main/install.sh | sh -s -- -b /usr/local/bin'
        script {
					docker.withRegistry('','DockerhubID') {
            sh 'docker-scout cves $IMAGE_TAG --exit-code --only-severity critical'
          }
        }
      }
    }
  }
}