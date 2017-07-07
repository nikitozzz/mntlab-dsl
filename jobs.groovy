job('EPBYMINW2629/MNTLAB-atsuranau-main-build-job') {
    description 'Build and test main job.'
    parameters {
	choiceParam('BRANCH_NAME', ['atsuranau', 'master'], 'Select branch')
        extendedChoiceParameterDefinition {
           name('Select job')
           type('checkbox')
          value('MNTLAB-atsuranau-child1-build-job,MNTLAB-atsuranau-child2-build-job,MNTLAB-atsuranau-child3-build-job,MNTLAB-atsuranau-child4-build-job')
 			 visibleItemCount(4)
          multiSelectDelimiter(',')
          propertyFile('')
          propertyKey('')
          defaultValue('')
          defaultPropertyFile('')
           defaultPropertyKey('')
			quoteValue(false) 
           description('') 
           bindFieldName('')
			svnPath(false)
			svnUrl('')
			svnUserName('')
			svnPassword('')
			projectName('')
			roleBasedFilter(false) 
        }
	}
    scm {
        github 'MNT-Lab/mntlab-dsl','atsuranau'
    }
    steps {

    }

}

def command = "git ls-remote -h https://github.com/MNT-Lab/mntlab-dsl.git"
def proc = command.execute()
proc.waitFor()
if ( proc.exitValue() != 0 ) {
    println "Error, ${proc.err.text}"
    System.exit(-1)
}
def branches = proc.in.text.readLines().collect {
    it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
}

(1..4).each {
	job("EPBYMINW2629/MNTLAB-atsuranau-child${it}-build-job") {
    description 'Build and test child job.'
    parameters {
	choiceParam('BRANCH_NAME', branches, 'Select git branch')
	}
    scm {
        github 'MNT-Lab/mntlab-dsl','atsuranau'
    }
    steps {
        shell('chmod +x script.sh && ./script.sh && ./script.sh > output.txt && tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz output.txt jobs.groovy script.sh')
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
