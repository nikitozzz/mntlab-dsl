job('MNTLAB-hpashuto-main-build-job') {
    description 'DSL task main job.'
    scm {
        github 'sheehan/job-dsl-playground'
    }
    steps {
        gradle 'test'
    }
    publishers {
        archiveJunit 'build/test-results/**/*.xml'
    }
}
(1..4).each {
            def jobN = it.value
            job("MNTLAB-hpashuto-child$jobN-build-job") {
                description "DSL task child$jobN job."
                scm {
                    github 'sheehan/job-dsl-playground'
                }
                steps {
                    gradle 'test'
                }
                publishers {
                    archiveJunit 'build/test-results/**/*.xml'
                }
            }
        }