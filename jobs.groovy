def project = 'MNT-Lab/mntlab-dsl'
def branchApi = new URL("https://api.github.com/repos/${project}/branches")
def branches = new groovy.json.JsonSlurper().parse(branchApi.newReader())

freeStyleJob('EPBYMINW2033/MNTLAB-hpashuto-main-build-job') {
    description 'DSL task main job.'
    scm {
        github ''
    }
    steps {
        g
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
                    choiceParam ('BRANCH_NAME', branches.name )
                }
                scm {
                    github ''
                }
                steps {
                    gradle 'test'
                }
                publishers {
                    archiveJunit ''
                }
            }
        }