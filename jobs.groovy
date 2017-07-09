//Block with mane job
job('EPBYMINW2466/MNTLAB-{akarzhou}-main-build-job') {
// Add github scm with two branches and checkbox with child jobs
    scm {
        github 'MNT-Lab/mntlab-dsl', '$BRANCH_NAME'
	}
     parameters {
     choiceParam('BRANCH_NAME', ['akarzhou', 'master'], 'Choose ich branch you want to use')
	      extendedChoiceParameterDefinition {
          name('Select job')
          type('multiselect')
	  visibleItemCount(4)
          value('MNTLAB-akarzhou-child1-build-job,MNTLAB-akarzhou-child2-build-job')
 	  multiSelectDelimiter(',')
          }
}
	triggers {
	scm ('H/3 * * * *)		
}
  steps {
     shell(' echo "Publish artefact for childs jobs"')
}
//Publishing artifact for chlid jobs
publishers {
        archiveArtifacts {
            pattern('script.sh')
            onlyIfSuccessful()
        }
downstreamParameterized {
            trigger('EPBYMINW2466/MNTLAB-{akarzhou}-child1-build-job, EPBYMINW2466/MNTLAB-{akarzhou}-child2-build-job') {
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
// Block with 4 child jobs
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

// Creating new 4 jobs
['1', '2', '3', '4'].each { suffix ->
job('EPBYMINW2466/MNTLAB-{akarzhou}-child' + suffix + '-build-job') {
	parameters {
	choiceParam('BRANCH_NAME', branches)
}
	triggers {
	scm ('H/3 * * * *)		
}	
//Copying artifact from main job
steps {
copyArtifacts('EPBYMINW2466/MNTLAB-{akarzhou}-main-build-job') {
            includePatterns('script.sh')
            targetDirectory('./')
            flatten()
            optional()
            buildSelector {
                latestSuccessful(true)
            }
}
//Run script, archivate output information and make artifacts
shell('chmod +x ./script.sh && ./script.sh > output.txt && tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz output.txt script.sh')
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
