job('MNTLAB-zvirinsky-main-build-job'){
	description 'Main Job'
//	scm {
//        github 'sheehan/job-dsl-playground'
//    }
//    triggers { 
//       scm 'H/5 * * * *' 
//	} 
    steps {
    	shell('echo Hello World!')
//    }
 //   publishers {
//        archiveJunit 'build/test-results/**/*.xml'
//    }
	parameters {
        booleanParam('FLAG', true)
        choiceParam('BRANCH_NAME', ['zvirinsky (default)', 'master'])
    }
}