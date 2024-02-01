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
                echo 'mvn --version'
                echo 'java --version'
            }  
        }
    }

}