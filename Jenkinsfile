pipeline {
    agent {
        docker { image 'maven:3.9.5' }
    }
    stages {
        stage ('Test') {
            steps {
                sh 'mvn --version'
            }
        }
    }
}