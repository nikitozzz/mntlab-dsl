job('EPBYMINW2629/MNTLAB-atsuranau-main-build-job') {
    description 'Build and test main job.'
    scm {
        github 'MNT-Lab/mntlab-dsl','atsuranau'
    }
    steps {
        gradle 'clean test'
    }
    publishers {
        archiveJunit 'build/test-results/**/*.xml'
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
	choiceParam('BRANCH_NAME', $branches, 'Select git branch')
	}
    scm {
        github 'MNT-Lab/mntlab-dsl','atsuranau'
    }
    steps {
        gradle 'clean test'
    }
    publishers {
        archiveJunit 'build/test-results/**/*.xml'
    }
}
}
	
