// https://www.yamllint.com/        validate yaml

pipeline {
    agent any
    tools {
      maven 'maven3'
      jdk 'jdk17'
      //docker 'docker24'
    }
    environment {
      SCANNER_HOME= tool 'sonar-scanner'
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
        stage('Code Compile') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('Unit Test') {
            steps {
                sh 'mvn test -DskipTests=true'
            }
        }
        stage('BuSonarQube Static Code Analysis') {
            steps {
                echo 'DEMO'
            }
        }
        // stage('Unit Test') {
        //     steps {
        //         sh 'mvn test -DskipTests=true'
        //     }
        // }
    }
}