def git1 = "MNT-Lab/mntlab-dsl"
/**
def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
def command = "git ls-remote -h $gitURL"
def proc = command.execute()
proc.waitFor()
if ( proc.exitValue() != 0 ) {
    println "Error, ${proc.err.text}"
    System.exit(-1)
}
def branches = proc.in.text.readLines().collect {
    it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
}
*/
freeStyleJob('EPBYMINW2033/MNTLAB-hpashuto-main-build-job') {
    description 'DSL task main job.'
    parameters {
        choiceParam("BRANCH_NAME", ['hpashuto', 'master'])
        activeChoiceParam('BUILDS_TRIGGER') {
            description('Allows user choose child builds')
            choiceType('CHECKBOX')
            groovyScript {
                script ('["MNTLAB-hpashuto-child1-build-job","MNTLAB-hpashuto-child2-build-job", "MNTLAB-hpashuto-child3-build-job", "MNTLAB-hpashuto-child4-build-job"]')
                sandbox (sandbox = true)
            }
        }
    }
    scm {
        github (git1, '$BRANCH_NAME')
    }
    steps {
        shell ('echo hello world')
    }
}
(1..4).each {
    def jobN = it.value
    freeStyleJob("EPBYMINW2033/MNTLAB-hpashuto-child$jobN-build-job") {
        description "DSL task child$jobN job."
        parameters {
            choiceParam("BRANCH_NAME", ['hpashuto', 'master'])
        }

        scm {
            github(git1, '$BRANCH_NAME')
        }
        steps {
            shell('chmod +x script.sh && bash -ex script.sh > output.txt && cat output.txt && tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt script.sh jobs.groovy')
        }
        publishers {
            archiveArtifacts {
                pattern('output.txt')
                pattern('${BRANCH_NAME}_dsl_script.tar.gz')
                onlyIfSuccessful()
            }
        }
    }
}