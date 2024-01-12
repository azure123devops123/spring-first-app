pipeline {

    agent {
        docker { 
            image 'maven:3.9.5'  
            args '--user root -v /var/run/docker.sock:/var/run/docker.sock'     // Mount Docker Socket to access the host's Docker Daemon
            }
    }
    stages {
        stage ('Checkout') {
            steps {
                sh 'mvn --version'
            }
        }
        stage ('Compile') {
            steps {
                sh "mvn clean compile"
            }
        }
    	stage ('Package') {
			steps {
                sh "mvn -f pom.xml clean package"           
                }
		}

        stage ('Build Docker Image') {
            environment {
                dockerHome = tool 'myDocker'
                PATH =  "$dockerHome/bin:$PATH"
            }
            steps {
                script {
                    dockerImage = docker.build("devopstech24/jenkins-spring-first-app:${env.BUILD_TAG}")
                }
            }
        }

        stage ('Push Docker Image in Dockerhub') {
            steps {
                script {
                    docker.withRegistry('','dockerhubID') {
                        dockerImage.push();
                    }
                }
            }
        }
        // stage ('Docker Image Build and Push') {
        //     environment {
        //             dockerImage = docker.build("devopstech24/jenkins-spring-first-app:${env.BUILD_TAG}")
        //         }
        //     steps {
        //         script {
        //             docker.withRegistry('','dockerhubID') {
        //                 dockerImage.push();
        //                 dockerImage.push('latest');
        //             }
        //         }
        //     }
        // }
///////
        // stage('Build and Push Docker Image') {
        // environment {
        //     DOCKER_IMAGE = "devopstech24/jenkins-spring-first-app:${BUILD_TAG}"
        //     REGISTRY_CREDENTIALS = credentials('dockerhubID')
        // }
        // steps {
        //     script {
        //         sh 'docker build -t ${DOCKER_IMAGE} .'
        //         def dockerImage = docker.image("${DOCKER_IMAGE}")
        //         docker.withRegistry('https://index.docker.io/v1/', "dockerhubID") {
        //             dockerImage.push()
        //         }
        //     }
        // }
        // }
/////////
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
    //             sh 'docker --version'
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

    //     stage ('Push Docker Image in Dockerhub') {
    //         steps {
    //             script {
    //                 docker.withRegistry('','dockerhubID') {
    //                     dockerImage.push();
    //                     dockerImage.push('latest');
    //                 }
    //             }
    //         }
    //     }
    }
}
// AFTER SUCCESSFULL IMAGE PUSH TO DOCKERHUB:                 docker.io/devopstech24/jenkins-spring-first-app:jenkins-spring-first-app-pipeline-34
// YOU CAN RUN AND TEST THE IMAGE:    docker run -p 8080:8080 docker.io/devopstech24/jenkins-spring-first-app:jenkins-spring-first-app-pipeline-34


