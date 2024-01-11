pipeline {
    agent {
        docker { image 'maven:3.9.5' }
    }
    environment {
        mavenHome = tool 'myMaven'
        PATH = "$mavenHome/bin:$PATH"
    }

    stages {
        stage ('Checkout') {
            steps {
                sh 'mvn --version'
            }
        }
		stage ('Build'){
			steps {
				sh "./mvnw clean package"                   
                }
		}
        // stage ('Compile') {
        //     steps {
        //         sh "mvn clean compile"
        //     }

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
