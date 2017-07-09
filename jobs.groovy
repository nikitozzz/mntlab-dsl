def childList = []
(1..4).each {
        childList << "EPBYMINW2695/MNTLAB-adoropei-child${it}-build-job"
}

def result = ("git ls-remote -h -t https://github.com/MNT-Lab/mntlab-dsl.git").execute()
def branchList = result.in.text.readLines().collect
        {
            it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
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
                        choiceParameter {
                                name('BRANCH_NAME')
                                script {
                                         groovyScript {
                                                script {
                                                        script("""def result = ("git ls-remote -h -t https://github.com/MNT-Lab/mntlab-dsl.git").execute()
                                                                  def branches = result.in.text.readLines().collect
                                                                  {
                                                                    	'"' + (it.replaceAll(/[a-z0-9]*\\trefs\\/heads\\//, '') as String) + '"'
                                                                  }
                                                                  branches  """)
                                                        sandbox(false)
                                                }
                                                fallbackScript {
                                                        script('')
                                                        sandbox(false)
                                                }
                                        }
                                }	
                                choiceType('PT_SINGLE_SELECT')
                            description('Choose jobs to trigger')
                                randomName('param-4712')
                                filterable(false)
                        }
   	    }
            scm {
        		github 'MNT-Lab/mntlab-dsl','${BRANCH_NAME}'
    		}
            steps {
                        shell( "chmod 777 script.sh" )
      			shell( "./script.sh > output.txt" )
                        shell( ''' if [ -f "jobs.groovy" ]; then 
                                                tar -czf $BRANCH_NAME_dsl_script.tar.gz jobs.groovy output.txt
                                        else    tar -czf $BRANCH_NAME_dsl_script.tar.gz output.txt
                                        fi ''' )
            }
            publishers {
       			archiveArtifacts '${BRANCH_NAME}_dsl_script.tar.gz, output.txt'
    		}
        }
    }
    steps {
        downstreamParameterized {
                trigger('$JOBS_TRIGGER') {
                        block{
                                buildStepFailure('FAILURE')
                                failure('FAILURE')
                                unstable('UNSTABLE')
                        }
                        parameters {
                                predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                        }
                }
        }
        
    }
    
}
