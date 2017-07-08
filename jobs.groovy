def childList = []
(1..4).each {
        childList << "EPBYMINW2695/MNTLAB-adoropei-child${it}-build-job"
}


job('EPBYMINW2695/MNTLAB-adoropei-main-build-job') {
    description 'Build and test the app.'
    parameters {
        choiceParam('BRANCH_NAME', ['adoropei', 'master'])
       	choiceParameter {
      		name('JOBS_TRIGGER')
      		script {
       			 groovyScript {
          			script {
            			script("${childList.collect{ '"' + it + '"'}}")
            			sandbox(true)
          			}
          			fallbackScript {
            			script('')
            			sandbox(false)
          			}
       	 		}
      		}	
      		choiceType('PT_CHECKBOX')
            description('Choose jobs to trigger')
      		randomName('param-4711')
      		filterable(false)
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
                        shell( 'tar -czf ${BRANCH_NAME}_dsl_script.tar.gz script.sh' )
            }
            publishers {
       			archiveArtifacts '${BRANCH_NAME}_dsl_script.tar.gz, output.txt'
    		}
        }
    }
    publishers {
        childList.each { name ->
        	flexiblePublish {
                        conditionalAction {
                                condition {
                                        shell('$JOBS_TRIGGER == ' + "*${name}*")
                                }
                                publishers {
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
        }
    }
    
}
