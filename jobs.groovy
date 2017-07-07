freeStyleJob('EPBYMINW2033/MNTLAB-hpashuto-main-build-job') {
    description 'DSL task main job.'
    scm {
        github ''
    }
    steps {
        shell ('echo hello world')
    }
    publishers {
        archiveJunit ''
    }
}
(1..4).each {
    def jobN = it.value
    freeStyleJob("EPBYMINW2033/MNTLAB-hpashuto-child$jobN-build-job") {
        description "DSL task child$jobN job."
        parameters {
            gitParam('BRANCH_NAME') {
                description('Revision commit SHA')
                type('BRANCH')
                defaultValue('hpashuto')
            }
        }
        scm {
            github ('MNT-Lab/mntlab-dsl')
        }
        steps {
            gradle 'test'
        }
        publishers {
            archiveJunit ''
        }
    }
}