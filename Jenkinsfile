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
// // Continuous Integration PIPELINE - NOT WORKING CODE
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
        SONAR_URL = "http://3.104.79.100:9000"
      }
      steps {
        withCredentials([string(credentialsId: 'SonarqubeID', variable: 'SONAR_AUTH_TOKEN')]) {
          sh 'mvn sonar:sonar -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.host.url=${SONAR_URL}'
        }
      }
    }
    // stage('Build and Push Docker Image') {
    //   environment {
    //     DOCKER_IMAGE = "devopstech24/jenkins-spring-first-app:${BUILD_NUMBER}"
    //     REGISTRY_CREDENTIALS = credentials('DockerhubID')
    //   }
    //   steps {
    //     script {
    //         sh 'docker build -t ${DOCKER_IMAGE} .'
    //         def dockerImage = docker.image("${DOCKER_IMAGE}")
    //         docker.withRegistry('https://index.docker.io/v1/', "DockerhubID") {
    //             dockerImage.push()
    //         }
    //     }
    //   }
    // }
  }
}