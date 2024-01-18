pipeline {
    agent any

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
        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }
    }
}