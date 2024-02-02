pipeline {
    agent{
        label 'dev'
    }
    tools{
        maven 'mvn3'
        jdk 'jdk17'
    }
    // environment {
    //     SCANNER_HOME = tool 'SonarScanner'        // we define this tool and we can use it below.
    //     //sonar = tool 'SonarScanner'
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

        stage ('Code Unit Test') {
            steps {
                sh 'mvn test -DskipTests=True'
            }
        }
        
        stage('SonarQube Code Analysis') {
            steps {
                withSonarQubeEnv('SonarScanner') {      // Pass only server name as an argument
                    sh 'mvn clean verify sonar:sonar'   // Analyzing a Maven project consists of running a Maven goal: sonar:sonar from the directory that holds the main project pom.xml
                }
            }
        }
    }

}