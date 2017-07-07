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
freeStyleJob('EPBYMINW1374/MNTLAB-dsilnyagin-main-build-job'){
    description 'Build and test the app.'
    publishers {
        downstream('EPBYMINW1374/MNTLAB-dsilnyagin-child1-build-job', 'SUCCESS')
	downstream('EPBYMINW1374/MNTLAB-dsilnyagin-child2-build-job', 'SUCCESS')
	downstream('EPBYMINW1374/MNTLAB-dsilnyagin-child3-build-job', 'SUCCESS')
	downstream('EPBYMINW1374/MNTLAB-dsilnyagin-child4-build-job', 'SUCCESS')
    }
    parameters {
	choiceParam("BRANCH_NAME", branches)
    }
}
['EPBYMINW1374/MNTLAB-dsilnyagin-child1-build-job',
 'EPBYMINW1374/MNTLAB-dsilnyagin-child2-build-job',
 'EPBYMINW1374/MNTLAB-dsilnyagin-child3-build-job',
 'EPBYMINW1374/MNTLAB-dsilnyagin-child4-build-job'
].each { 
    freeStyleJob(it) {
    	description 'Build and test the app.'
	steps {
            shell('cp /var/server/config/jenkins/workspace/EPBYMINW1374/mntlab-ci-dsl/script.sh ./script.sh')
	    shell('chmod +x ./script.sh')
	    shell('./script.sh')
        }
    }
}
