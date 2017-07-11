def command = "git ls-remote -h https://github.com/MNT-Lab/mntlab-dsl.git" as java.lang.Object
def proc = command.execute()
proc.waitFor()
if ( proc.exitValue() != 0 ) {
    println "Error, ${proc.err.text}"
    System.exit(-1)
}
def branches = proc.in.text.readLines().collect {
    it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
}
job('EPBYMINW3088/MNTLAB-aaksionkin-DSL-build-job') {
    description 'Create child jobs.'
    parameters {
        //choiceParam(String parameterName, List<String> options, String description)
        choiceParam('BRANCH_NAME', ['aaksionkin', 'master'])
        activeChoiceParam('BUILDS_TRIGGER') {
            description('Available options')
            choiceType('CHECKBOX')
            groovyScript {
                script('["MNTLAB-aaksionkin-child1-build-job", "MNTLAB-aaksionkin-child2-build-job", "MNTLAB-aaksionkin-child3-build-job", "MNTLAB-aaksionkin-child4-build-job"]')
            }
        }
    }
        triggers {
            scm('H/5 * * * *')
        }

    scm {
        github 'MNT-Lab/mntlab-dsl','$BRANCH_NAME'
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
        //shell('chmod +x script.sh && ./script.sh > output.txt && cat output.txt && tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt')
    }
    publishers {
        archiveArtifacts('output.txt')
    }
    }



        //creating child jobs
    1.upto(4) {
        job("EPBYMINW3088/MNTLAB-aaksionkin-child${it}-build-job") {
            description 'Echo the shell.sh.'
            parameters {
                choiceParam('BRANCH_NAME', branches, 'Select git branch')
                /*gitParam('BRANCH_NAME') {
                    description('branch selection')
                    type('BRANCH')
                    //branch('~ /*')
                    //defaultValue('/aaksionkin')
                    sortMode('ASCENDING')
                }*/
            }
            scm {
                git {
                    remote {
                        name('mntlab-dsl')
                        url('https://github.com/MNT-Lab/mntlab-dsl.git')
                    }
                    branch('$BRANCH_NAME')
                }
                triggers {
                    scm 'H/5 * * * *'
                }
            }
            steps {
                shell('chmod +x script.sh && ./script.sh > output.txt && cat output.txt && ' +
                        'tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt jobs.groovy')
            }
        }
    }



