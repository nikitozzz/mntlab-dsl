def childList = []
(1..4).each {
        childList << "EPBYMINW2695/MNTLAB-adoropei-child${it}-build-job"
}


job('EPBYMINW2695/MNTLAB-adoropei-main-build-job') {
    description 'Build and test the app.'
    parameters {
        choiceParam('BRANCH_NAME', ['adoropei', 'master'])
        activeChoiceParam('JOBS_TRIGGER') {
            description('Allows user choose from multiple choices')
            choiceType('CHECKBOX')
            groovyScript {
                script("${childList}")
                sandbox(true)
            }
        }
   	}
    childList.each {
        job(it){
            parameters {
        		choiceParam('BRANCH_NAME', ['adoropei', 'master'])
   			}
            scm {
        		github 'MNT-Lab/mntlab-dsl','${BRANCH_NAME}'
    		}
        	steps {
                shell( "chmod 777 script.sh" )
      			shell( "./script.sh > output.txt" )
                shell( 'tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt' )
            }
            publishers {
       			archiveArtifacts '${BRANCH_NAME}_dsl_script.tar.gz'
    		}
        }
    }
    publishers {
        childList.each { name ->
        	downstreamParameterized {
                	trigger(name) {
                	condition('SUCCESS')
                	parameters {
                   		predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
               		}
            	}
        	}
        }
    }
    
}
