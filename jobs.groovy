def childList = []
(1..4).each {
        childList << "EPBYMINW2695/MNTLAB-adoropei-child${it}-build-job"
}


job('EPBYMINW2695/MNTLAB-adoropei-main-build-job') {
    description 'Build and test the app.'
    childList.each {
        job(it){
            scm {
        		github 'MNT-Lab/mntlab-dsl','adoropei'
    		}
        	steps {
                shell( 'BRANCH_NAME="adoropei"' )
                shell( "chmod 777 script.sh" )
      			shell( "./script.sh > output.txt" )
            }
            publishers {
       			archiveArtifacts 'output.txt'
    		}
        }
    }
    publishers {
        childList.each {
        	downstream (it, 'SUCCESS')
        }
    }
}
