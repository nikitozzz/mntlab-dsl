pipeline{
    agent any
    stages {
        stage('Stack creation'){
           steps {
                jobDsl targets: "$WORKSPACE/jobs.groovy"
            }
        }
    }
}
