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
(1..4).each {
	job('EPBYMINW2629/MNTLAB-atsuranau-child$it-build-job') {
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
}
	
