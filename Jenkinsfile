pipeline{
    agent any
    stages {
        stage('Stack creation'){
          jobDsl targets: "$WORKSPACE/jobs.groovy"
        }
    }
}
