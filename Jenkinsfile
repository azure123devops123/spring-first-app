// pipeline {
//     agent any

//     stages {
//         stage('Hello') {
//             steps {
//                 echo 'Hello World'
//             }
//         }
//     }
// }

pipelines {
  agent{
      any
  }
  stages{
      stage ('Clean Up') {
        steps {
          cleanWs()
        }
      }
  }
}