def childList = []
(1..4).each {
        childList << "EPBYMINW2695/MNTLAB-adoropei-child${it}-build-job"
}


job('EPBYMINW2695/MNTLAB-adoropei-main-build-job') {
    description 'Build and test the app.'
    parameters {
        	choiceParam('BRANCH_NAME', ['adoropei', 'master'])
   	}
    scm {
        github 'sheehan/job-dsl-playground'
    }
    steps {
        gradle 'test'
    }
    childList.each {
        job(it){
            parameters {
        	        choiceParam('BRANCH_NAME', ['adoropei', 'master'])
   			}
            scm {
        		github 'MNT-Lab/mntlab-dsl','adoropei'
    		}
        	steps {
                shell( "chmod 777 script.sh" )
      			shell( "./script.sh > output.txt" )
                shell( 'tar -czvf $BRANCH_NAME_dsl_script.tar.gz *' )
            }
            publishers {
       			archiveArtifacts 'output.txt'
    		}
        }
    }
    publishers {
        childList.each {
        	downstreamParameterized {
                trigger("${it}") {
                	condition('SUCCESS')
                	parameters {
                   		predefinedProp('BRANCH_NAME', 'adoropei')
               		}
            	}
        	}
        }
    }
}
