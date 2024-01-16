// Continuous Integration PIPELINE - MY WORKING CODE...

pipeline {

  environment {
    DOCKER_USER = "Java-Application-Code-Jenkins-Continuous-Integration"
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
          // Install Docker inside Container     
          sh 'curl -fsSLO https://download.docker.com/linux/static/stable/x86_64/docker-24.0.7.tgz && tar --strip-components=1 -xvzf docker-24.0.7.tgz -C /usr/local/bin'   // YOU CAN FIND CURRENT BINAARY VERSION THEN DOWNLOAD AND INSTALL BELOW: https://download.docker.com/linux/static/stable/x86_64/   => docker-24.0.7.tgz
             
					docker.withRegistry('','DockerhubID') {
              dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
					} // end of wrapper (withRegistry)

          docker.withRegistry('','DockerhubID') {
					    dockerImage.push();
					    dockerImage.push('latest')   // We can't push without Jenkins having Docker Hub Credentials (DockerID and Token (note:-Password will not work).
					} // end of wrapper (withRegistry)
				}
			}
		}

    stage ('Analyze Image - Docker Scout Image Scanner') {
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