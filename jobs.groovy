tyleJob('EPBYMINW1374/MNTLAB-dsilnyagin-main-build-job'){
    description 'Build and test the app.'
}
['EPBYMINW1374/MNTLAB-dsilnyagin-child1-build-job',
 'EPBYMINW1374/MNTLAB-dsilnyagin-child2-build-job',
 'EPBYMINW1374/MNTLAB-dsilnyagin-child3-build-job',
 'EPBYMINW1374/MNTLAB-dsilnyagin-child4-build-job'
].each { 
    freeStyleJob(it) {
    	description 'Build and test the app.'
	}
}
