def git = "MNT-Lab/mntlab-dsl"
def repo = "ndolya"
def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
def command = "git ls-remote -h $gitURL"

def proc = command.execute()
proc.waitFor()

if ( proc.exitValue() != 0 ) {
    println "Error, ${proc.err.text}"
    System.exit(-1)
}

def branches = proc.in.text.readLines().collect {
    it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
}

job("EPBYMINW1969/MNTLAB-$repo-main-build-job") {
    parameters {
	choiceParam('BRANCH_NAME', ['ndolya', 'master'],'Choose git branch')
	    activeChoiceParam('BUILDS_TRIGGER') {
            description('Available options')
            choiceType('CHECKBOX')
            groovyScript {
            script('["MNTLAB-ndolya-child1-build-job", "MNTLAB-ndolya-child2-build-job", "MNTLAB-ndolya-child3-build-job", "MNTLAB-ndolya-child4-build-job"]')
            }
	  }
    }
    scm {
        github(git, '$BRANCH_NAME')
    }
	steps {
        downstreamParameterized {
            trigger('$BUILDS_TRIGGER') {
                block {
                    buildStepFailure('FAILURE')
                    failure('FAILURE')
                    unstable('UNSTABLE')
                }
               parameters {
                    currentBuild()
		}
	    }
	}	
       
    }
   
    
}

1.upto(4) {
  job("EPBYMINW1969/MNTLAB-$repo-child${it}-build-job") {
	  parameters {
        choiceParam('BRANCH_NAME', branches)
    }
    scm {
        github(git, '$BRANCH_NAME')
    }
      steps {
          copyArtifacts('EPBYMINW1969/MNTLAB-ndolya-main-build-job') {
              includePatterns('script.sh')
              targetDirectory('./')
              flatten()
              optional()
              buildSelector {
                  latestSuccessful(true)
              }
          }
      }
    steps {
        shell('''chmod +x script.sh 
	./script.sh script.sh > output.txt
	 cat output.txt
	  if [ -f jobs.groovy ]
      then
      tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt script.sh jobs.groovy
      else
      tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt script.sh
      fi
	 ''')
    }
	  publishers {
            archiveArtifacts {
                pattern('output.txt')
                pattern('${BRANCH_NAME}_dsl_script.tar.gz')
                onlyIfSuccessful()
            }
	  }
}

}


