pipeline {
    agent{
        label 'dev'
    }
    tools{
        maven 'mvn3'
        jdk 'jdk17'
    }
    // environment{

    // }
    stages{
        stage ('Workspace Cleanup') {
            steps {
                cleanWs()
            }  
        }
        stage ('Git Checkout') {
            steps {
                sh 'mvn --version'
                sh 'java --version'
                git branch: 'main', credentialsId: 'git-cred', url: 'https://github.com/azure123devops123/spring-first-app'
                sh 'ls'
            }  
        }
        stage ('Code Compile') {
            steps {
                sh 'mvn clean compile'
            }
        } 
        stage ('Code Test') {
            steps {
                sh 'mvn test -DskipTests=True'
            }
        }    
    }

}