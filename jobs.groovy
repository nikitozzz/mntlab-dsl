job('EPBYMINW2695/MNTLAB-adoropei-main-build-job') {
    description 'Build and test the app.'
    scm {
        github 'sheehan/job-dsl-playground'
    }
    steps {
        gradle 'test'
    }
    (1..4).each {
        job("EPBYMINW2695/MNTLAB-adoropei-child${it}-build-job"){
            scm {
        		github 'MNT-Lab/mntlab-dsl','adoropei'
    		}
        	steps {
                shell( "chmod 777 script.sh" )
      			shell( "./script.sh > output.txt" )
            }
        }
    }
    publishers {
        archiveJunit 'build/test-results/**/*.xml'
    }
}
