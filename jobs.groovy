freeStyleJob('EPBYMINW1374/MNTLAB-dsilnyagin-main-build-job'){
    description 'Build and test the app.'
    publishers {
        downstream('EPBYMINW1374/MNTLAB-dsilnyagin-child1-build-job', 'SUCCESS')
	downstream('EPBYMINW1374/MNTLAB-dsilnyagin-child2-build-job', 'SUCCESS')
	downstream('EPBYMINW1374/MNTLAB-dsilnyagin-child3-build-job', 'SUCCESS')
	downstream('EPBYMINW1374/MNTLAB-dsilnyagin-child4-build-job', 'SUCCESS')
    }
    parameters {
	choiceParam('OPTION', ['option 1 (default)', 'option 2', 'option 3'])
        activeChoiceParam('CHOICE-1') {
            description('Allows user choose from multiple choices')
            choiceType('CHECKBOX')
            groovyScript {
                script('["choice1", "choice2"]')
                fallbackScript('"fallback choice"')
            }
        }
    }
}
['EPBYMINW1374/MNTLAB-dsilnyagin-child1-build-job',
 'EPBYMINW1374/MNTLAB-dsilnyagin-child2-build-job',
 'EPBYMINW1374/MNTLAB-dsilnyagin-child3-build-job',
 'EPBYMINW1374/MNTLAB-dsilnyagin-child4-build-job'
].each { 
    freeStyleJob(it) {
    	description 'Build and test the app.'
	environmentVariables {
	    script('cp /var/server/config/jenkins/workspace/EPBYMINW1374/mntlab-ci-dsl ./script.sh')
	    script('chmod +x script.sh')
	    script('script.sh')
    	}
    }
}
