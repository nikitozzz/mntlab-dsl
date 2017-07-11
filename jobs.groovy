def git = 'MNT-Lab/mntlab-dsl'
def repo = '$BRANCH_NAME'
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

job('EPBYMINW1766/MNTLAB-amaslakou-main-build-job') {
    scm {
        github(git, repo)
    }
    parameters {
        choiceParam('BRANCH_NAME', ['amaslakou', 'master'])

        activeChoiceParam('BUILDS_TRIGGER') {
            description('Choose: ')
            choiceType('CHECKBOX')
            groovyScript {
                script('["MNTLAB-amaslakou-child-1-build-job", "MNTLAB-amaslakou-child-2-build-job", "MNTLAB-amaslakou-child-3-build-job", "MNTLAB-amaslakou-child-4-build-job"]')
            }
        }
    }
    steps {
        downstreamParameterized {
            trigger('$BUILDS_TRIGGER') {
                block {
                    buildStepFailure('FAILURE')
                    failure('FAILURE')
                    unstable('UNSTABLE')
                }
                parameters {
                    currentBuild()
                }
            }
        }
    }

    (1..4).each {
        println "Job Number: ${it}"
        job("EPBYMINW1766/MNTLAB-amaslakou-child-${it}-build-job") {
            scm {
                github(git, repo)
            }
            parameters { choiceParam('BRANCH_NAME', branches) }
            steps {
                shell('chmod +x ./script.sh; ./script.sh; ./script.sh >> output.txt; tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz output.txt script.sh')
            }
            publishers {
                archiveArtifacts {
                    pattern('${BRANCH_NAME}_dsl_script.tar.gz')
                    pattern('output.txt')
                    onlyIfSuccessful()
                }
            }
        }
    }
}

