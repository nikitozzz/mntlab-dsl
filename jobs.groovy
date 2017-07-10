//Yuri Shchanouski
def git = "MNT-Lab/mntlab-dsl"
def repo = "yshchanouski"

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

job("EPBYMINW2468/MNTLAB-yshchanouski-main-build-job") {
    logRotator {
        numToKeep(5)
        artifactNumToKeep(5)
    }
    parameters {
	choiceParam('BRANCH_NAME', ['yshchanouski', 'master'])
        activeChoiceParam('BUILDS_TRIGGER') {
            description('Available options')
            filterable()
            choiceType('CHECKBOX')
            groovyScript {
                script('["MNTLAB-yshchanouski-child1-build-job", "MNTLAB-yshchanouski-child2-build-job", "MNTLAB-yshchanouski-child3-build-job", "MNTLAB-yshchanouski-child4-build-job"]')
            }
        }
    }
    scm {
        github(git, '$BRANCH_NAME')
    }
    triggers {
        scm('H/5 * * * *')
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
        shell('chmod +x script.sh && ./script.sh > output.txt && cat output.txt && tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt')
    }
    publishers { 
	archiveArtifacts('output.txt')
    }



}

1.upto(4) {
job("EPBYMINW2468/MNTLAB-yshchanouski-child${it}-build-job") {
    logRotator {
        numToKeep(5)
        artifactNumToKeep(5)
    }
    parameters {
	choiceParam('BRANCH_NAME', branches)
    }
    scm {
        github(git, '$BRANCH_NAME')
    }
    steps {
        shell('chmod +x script.sh && ./script.sh > output.txt && cat output.txt && tar -czf  ${BRANCH_NAME}_dsl_script.tar.gz output.txt jobs.groovy script.sh')
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
