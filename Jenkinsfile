pipeline {
    
    agent {
        docker { image 'maven:3.9.6' }
    }
    stages {
        stage ('Checkout') {
            steps {
                sh 'mvn --version'
                sh 'docker version'
            }
        }
    }

    // agent any
	// environment {    // GO INSIDE Manage Jenkins and get the names of both tools we set earlier (myDocker & myMaven)
	// 	dockerHome = tool 'myDocker'
	// 	mavenHome = tool 'myMaven'
	// 	PATH =  "$dockerHome/bin:$mavenHome/bin:$PATH"      // add both tools to our path
	// }  

    // stages {
    //     stage ('Checkout') {
    //         steps {
    //             sh 'mvn --version'
    //             sh 'docker version'
    //         }
    //     }

    //     stage ('Compile') {
    //         steps {
    //             sh "mvn clean compile"
    //         }
    //     }

	// 	stage ('Package') {
	// 		steps {
    //             sh "mvn -f pom.xml clean package"           
    //             }
	// 	}

    //     stage ('Build Docker Image') {
    //         steps {
    //             script {
    //                 dockerImage = docker.build("devopstech24/jenkins-spring-first-app:${env.BUILD_TAG}")
    //             }
    //         }
    //     }

    //     stage ('Push Docker Image') {
    //         steps {
    //             script {
    //                 docker.withRegistry('','dockerhubID') {
    //                     dockerImage.push();
    //                     dockerImage.push('latest');
    //                 }
    //             }
    //         }
    //     }
    // }
}

// AFTER SUCCESSFULL IMAGE PUSH TO DOCKERHUB:                 docker.io/devopstech24/jenkins-spring-first-app:jenkins-spring-first-app-pipeline-34
// YOU CAN RUN AND TEST THE IMAGE:    docker run -p 8080:8080 docker.io/devopstech24/jenkins-spring-first-app:jenkins-spring-first-app-pipeline-34
