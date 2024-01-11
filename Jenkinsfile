pipeline {
    agent {
        docker { image 'openjdk:23-jdk-slim-bullseye' }
    }
	// environment {    // GO INSIDE Manage Jenkins and get the names of both tools we set earlier (myDocker & myMaven)
	// 	dockerHome = tool 'myDocker'
	// 	mavenHome = tool 'myMaven'
	// 	PATH =  "$dockerHome/bin:$mavenHome/bin:$PATH"      // add both tools to our path
	// }  

    stages {
        stage ('Checkout') {
            steps {
                // sh 'mvn --version'
                sh 'java --version'
            }
        }

        // stage ('Compile') {
        //     steps {
        //         sh "mvn -X -e clean compile"
        //     }
        // }

		// stage ('Build') {
		// 	steps {
		// 		sh "./mvnw clean package"
        //         // sh "mvn -f pom.xml clean package"           
        //         }
		// }

        // }
        // stage ('Package') {
        //     steps {
        //         sh "./mvnw clean package"
        //     }
        // }
        // stage ('Build Docker Image') {
        //     steps {
        //         script {
        //             dockerImage = docker.build("devopstech24/jenkins-spring-first-app:${env.BUILD_TAG}")
        //         }
        //     }
        // }
    }
}
