def git = "MNT-Lab/mntlab-dsl"
def myRepo = "dsilnyagin"
def defRepo = "master"

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
def mainBr = []
branches.each { if( it == myRepo || it == defRepo) { mainBr.add(it) } }

freeStyleJob('EPBYMINW1374/MNTLAB-dsilnyagin-main-build-job'){
    description 'Build and test the app.'
    publishers {
	archiveArtifacts {
	    pattern('script.sh')	
	}
        downstream('EPBYMINW1374/MNTLAB-dsilnyagin-child1-build-job', 'SUCCESS')
	downstream('EPBYMINW1374/MNTLAB-dsilnyagin-child2-build-job', 'SUCCESS')
	downstream('EPBYMINW1374/MNTLAB-dsilnyagin-child3-build-job', 'SUCCESS')
	downstream('EPBYMINW1374/MNTLAB-dsilnyagin-child4-build-job', 'SUCCESS')
    }
    parameters {
	choiceParam("BRANCH_NAME", mainBr)
    }
    scm {
	github 'MNT-Lab/mntlab-dsl', '$BRANCH_NAME'
    }
}
['EPBYMINW1374/MNTLAB-dsilnyagin-child1-build-job',
 'EPBYMINW1374/MNTLAB-dsilnyagin-child2-build-job',
 'EPBYMINW1374/MNTLAB-dsilnyagin-child3-build-job',
 'EPBYMINW1374/MNTLAB-dsilnyagin-child4-build-job'
].each { 
    freeStyleJob(it) {
    	description 'Build and test the app.'
	parameters {
	    choiceParam("BRANCH_NAME", branches)
    	}
	steps {
	    copyArtifacts('EPBYMINW1374/MNTLAB-dsilnyagin-main-build-job') {
        	includePatterns('*.sh')
                targetDirectory('./')
                flatten()
                optional()
                buildSelector {
                    latestSuccessful(true)
                }
    	    }
	    shell('chmod +x ./script.sh; ./script.sh; ./script.sh >> output.txt; tar -czvf $BRANCH_NAME_dsl_script.tar.gz output.txt script.sh')
	    publishers {
	        archiveArtifacts {
	            pattern('script.sh')
		    pattern('output.txt')	
	        }
            }
	}
    }
}
