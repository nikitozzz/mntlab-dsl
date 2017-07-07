tyleJob('Daniil Silnyagin/MNTLAB-dsilnyagin-main-build-job'){
    description 'Build and test the app.'
}
['Daniil Silnyagin/MNTLAB-dsilnyagin-child1-build-job',
 'Daniil Silnyagin/MNTLAB-dsilnyagin-child2-build-job',
 'Daniil Silnyagin/MNTLAB-dsilnyagin-child3-build-job',
 'Daniil Silnyagin/MNTLAB-dsilnyagin-child4-build-job'
].each { 
    freeStyleJob(it) {
    	description 'Build and test the app.'
	}
}
