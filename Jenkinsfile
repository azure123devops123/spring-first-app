pipeline {
    agent {
        docker { image 'maven:3.9.5' }
    }
    stages {
        stage ('Checkout') {
            steps {
                sh 'mvn --version'
            }
        }
        stage ('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("devopstech24/jenkins-spring-first-app:${env.BUILD_TAG}")
                }
            }
        }
    }
}