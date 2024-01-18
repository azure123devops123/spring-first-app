pipeline {
    agent any
    tools {
      maven 'maven3'
      jdk 'jdk17'
      //docker 'docker24'
    }
    stages {
        stage('Cleanup Workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Git Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/azure123devops123/spring-first-app'
                }
        }
        stage('Build and Test') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}