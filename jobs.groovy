def gitrepo = 'MNT-Lab/mntlab-dsl'
def branchname = 'hpashuto'

/** Geting list of branches*/
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
def jobcount = (1..4)
def joblist = []
jobcount.each {joblist.add("MNTLAB-hpashuto-child$it-build-job")}

freeStyleJob('EPBYMINW2033/MNTLAB-hpashuto-main-build-job') {
    description 'DSL task main job.'
    parameters {
        choiceParam("BRANCH_NAME", ['hpashuto', 'master'])
        activeChoiceParam('BUILDS_TRIGGER') {
            description('Allows user choose child builds')
            choiceType('CHECKBOX')
            groovyScript {
                script("$joblist")
                //script ('["MNTLAB-hpashuto-child1-build-job","MNTLAB-hpashuto-child2-build-job", "MNTLAB-hpashuto-child3-build-job", "MNTLAB-hpashuto-child4-build-job"]')
                //sandbox (true)
            }
        }
    }
    scm {
        github (gitrepo, '$BRANCH_NAME')
    }
    steps {
        conditionalSteps {
            condition {
                alwaysRun()
            }
            runner('Fail')
            steps {



                jobcount.each {
                    def Jn = it.value
                    conditionalSteps {
                        condition {
                            expression("(?is).*child$Jn.*",  '${BUILDS_TRIGGER}')
                        }
                        runner('Fail')
                        steps {

                            downstreamParameterized {
                                trigger("MNTLAB-hpashuto-child$Jn-build-job") {
                                    block {
                                        buildStepFailure('FAILURE')
                                        failure('FAILURE')
                                        unstable('UNSTABLE')
                                    }
                                    parameters {
                                        predefinedProp('BRANCH_NAME', '${BRANCH_NAME}')
                                    }
                                }
                            }

                        }
                    }
                }




            }
        }

    }
}
jobcount.each {
    def jobN = it.value
    freeStyleJob("EPBYMINW2033/MNTLAB-hpashuto-child$jobN-build-job") {
        description "DSL task child$jobN job."
        parameters {
            choiceParam("BRANCH_NAME", branches)
        }

        scm {
            github(gitrepo, '$BRANCH_NAME')
        }
        steps {
            shell('''chmod +x script.sh 
bash -ex script.sh > output.txt
cat output.txt
if [[ -f jobs.groovy ]]
then
tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt script.sh jobs.groovy
else
tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt script.sh
fi''')
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