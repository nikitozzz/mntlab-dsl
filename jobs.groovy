//Block with mane job
job('EPBYMINW2466/MNTLAB-{akarzhou}-main-build-job') {
    scm {
        git {
            remote {
                name('remoteB')
                url('https://github.com/MNT-Lab/mntlab-dsl.git')
            }
            branches('akarzhou', 'master')                    
        }      
    }
  steps {
      			shell( "./script.sh" )
}   
  parameters {
     choiceParam('BRANCH_NAME', ['akarzhou', 'master'], 'Choose appropriate branch')
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

   scm {
        github 'MNT-Lab/mntlab-dsl', '$BRANCH_NAME'
}
steps {
shell( "./script.sh > output.txt; tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz ./output.txt")
}
      publishers {
        archiveArtifacts {
            pattern('output.txt')
          	pattern('$BRANCH_NAME_dsl_script.tar.gz')
            onlyIfSuccessful()
        }
    }
  
}
}
