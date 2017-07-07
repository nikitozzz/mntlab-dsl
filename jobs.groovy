freeStyleJob('MNTLAB-dsilnyagin-main-build-job'){
    description 'Build and test the app.'
}
['MNTLAB-dsilnyagin-child1-build-job',
 'MNTLAB-dsilnyagin-child2-build-job',
 'MNTLAB-dsilnyagin-child3-build-job',
 'MNTLAB-dsilnyagin-child4-build-job'
].each { 
    freeStyleJob(it) {
    	description 'Build and test the app.'
	}
}
