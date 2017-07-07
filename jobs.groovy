job('./EPBYMINW2472/MNTLAB-zvirinsky-main-build-job'){
	description 'Main Job'
//	scm {
//        github 'sheehan/job-dsl-playground'
//    }
//    triggers { 
//       scm 'H/5 * * * *' 
//	} 
    steps {
    	shell('echo Hello World!')
    }
 //   publishers {
//        archiveJunit 'build/test-results/**/*.xml'
//    }
	parameters {
        choiceParam('BRANCH_NAME', ['zvirinsky (default)', 'master'])
        booleanParam('MNTLAB-zvirinsky-child1-build-job', true)
        booleanParam('MNTLAB-zvirinsky-child2-build-job', true)
        booleanParam('MNTLAB-zvirinsky-child3-build-job', true)
        booleanParam('MNTLAB-zvirinsky-child4-build-job', true)
        
    }
}
