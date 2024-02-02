pipeline {
    agent {
        label 'dev'
    }
    tools {
        maven 'mvn3'
        jdk 'jdk17'
        // docker 'docker24'            // we do not have to mention this in tools otherwise we will get error
    }
    // environment {
    //     SCANNER_HOME = tool 'SonarScanner'        // we define this tool and we can use it below.
    // }
    stages {
        stage ('Workspace Cleanup') {
            steps {
                cleanWs()
            }  
        }

        stage ('Git Checkout') {
            steps {
                sh 'mvn --version'
                sh 'java --version'
                sh 'docker --version'
                git branch: 'main', credentialsId: 'git-cred', url: 'https://github.com/azure123devops123/spring-first-app'
                sh 'ls'
            }  
        }

        stage ('Code Compile') {
            steps {
                sh 'mvn clean compile'      // code compile 
            }
        }

        stage ('Code Unit Test') {
            steps {
                sh 'mvn test -DskipTests=True'  // skip tests but not in production :)
            }
        }

        stage('SonarQube Code Analysis') {
            steps {
                withSonarQubeEnv('SonarScanner') {      // Pass only server name as an argument
                    sh 'mvn clean verify sonar:sonar'   // Analyzing a Maven project consists of running a Maven goal: sonar:sonar from the directory that holds the main project pom.xml
                }
            }
        }

        stage('OWASP Dependencies Check') {   // It will take 5 to 10 minutes when we run for the first time because it will download the National Vulnerability Database (NVD) from 2002 to onwards
            steps {
                // dependencyCheck additionalArguments: '--scan ./', odcInstallation: 'DC'          // WE CAN USE THIS ONE LINER CODE AND IT WILL WORK FINE BUT BELOW CODE IS GENRATED BY Snippet Generator.
                dependencyCheck additionalArguments: '''--project	spring-first-app-jenkins-ci-pipeline
                --scan	./
                --format	XML''', odcInstallation: 'DC'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }

    }

}