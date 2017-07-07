job('EPBYMINW2629/MNTLAB-atsuranau-main-build-job') {
    description 'Build and test main job.'
    scm {
        github 'MNT-Lab/mntlab-dsl','atsuranau'
    }
   triggers { 
       scm 'H/5 * * * *' 
   }
    steps {
        gradle 'clean test'
    }
    publishers {
        archiveJunit 'build/test-results/**/*.xml'
    }
}
