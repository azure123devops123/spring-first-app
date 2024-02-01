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
            steps{
                cleanWs()
            }  
        }
        stage ('Git Checkout') {
            steps{
                sh 'mvn --version'
                sh 'java --version'
            }  
        }
    }

}