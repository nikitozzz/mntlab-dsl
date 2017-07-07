job('MNTLAB-adoropei-main-build-job') {
    description 'Build and test the app.'
    scm {
        github 'sheehan/job-dsl-playground'
    }
    steps {
        gradle 'test'
    }
    ['MNTLAB-adoropei-child1-build-job',
    'MNTLAB-adoropei-child2-build-job',
    'MNTLAB-adoropei-child3-build-job',
     'MNTLAB-adoropei-child4-build-job'].each {
        job(it){
            scm {
        		github 'https://github.com/MNT-Lab/mntlab-dsl.git'
    		}
        	steps {
      			shell( "./script.sh" )
            }
        }
    }
    publishers {
        archiveJunit 'build/test-results/**/*.xml'
    }
}
        