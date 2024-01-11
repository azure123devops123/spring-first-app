pipeline {
    agent any
    // agent {
    //     docker { image 'maven:latest' }
    // }
	environment {    // GO INSIDE Manage Jenkins and get the names of both tools we set earlier (myDocker & myMaven)
		dockerHome = tool 'myDocker'
		mavenHome = tool 'myMaven'
		PATH =  "$dockerHome/bin:$mavenHome/bin:$PATH"      // add both tools to our path
	}  

    stages {
        stage ('Checkout') {
            steps {
                sh 'mvn --version'
            }
        }

        stage ('Compile') {
            steps {
                sh "mvn clean compile"
            }
        }

		stage ('Package') {
			steps {
                sh "mvn -f pom.xml clean package"           
                }
		}

        stage ('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("devopstech24/jenkins-spring-first-app:${env.BUILD_TAG}")
                }
            }
        }

        stage ('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('','dockerhubID') {
                        docker.push();
                        docker.push('latest');
                    }
                }
            }
        }
    }
}
