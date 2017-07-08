def gitrepo = 'MNT-Lab/mntlab-dsl'
def branchname = 'vtarasiuk'

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

    /** Name Section **/

/** Setting list of master parameters  (hardcode)*/
String student = 'vtarasiuk'; String master = 'master'
def masterchoice = [student, master]

/** Setting master-job name*/
def lord = 'MNTLAB-vtarasiuk-main-build-job'

/** Common Folder name*/
def folder = 'EPBYMINW2471'

/** Setting list of child job names  (hardcode)*/
def jbn = []; def currentJob = []
for (i in 1..4){
    jbn.add("MNTLAB-vtarasiuk-child${i}-build-job")
}
def someScript = (''' 
def some = []
for (i in 1..4){
    some.add("MNTLAB-vtarasiuk-child${i}-build-job")
}
return some''')
def otherScript = (''' 
if [ ! -e "jobs.groovy" ] 
then  tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt script.sh
else tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt jobs.groovy script.sh 
fi''')

    /**Job Section**/

/** Create Master job*/
job("${folder}/${lord}") {
    parameters {
        choiceParam('BRANCH_NAME', masterchoice)
        activeChoiceParam('BUILDS_TRIGGER') {
            choiceType('CHECKBOX')
            description('You may choose some jobs to build. Choose wise...')
            groovyScript {
                script(someScript)
            }
        }
    }
    scm {
        github(gitrepo, branchname)
    }

    steps {
        for (j in 1..4) {
            conditionalSteps {
                condition {
                    expression("(?is).*child$j.*", '${BUILDS_TRIGGER}')
                }
                runner('Fail')
                steps {
                    currentJob.add ('${BUILDS_TRIGGER}')
                }
            }
        }
        downstreamParameterized {
            trigger('${BUILDS_TRIGGER}') {
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

}
/** Create child jobs*/
jbn.each {
    job("${folder}/${it}") {
        parameters {
            choiceParam('BRANCH_NAME', branches)
        }
        scm {
            github(gitrepo, branchname)
        }
        steps {

            shell ('chmod +x script.sh && ./script.sh > output.txt && cat output.txt')
            shell (otherScript)
        }
        publishers {
            archiveArtifacts('output.txt')
            archiveArtifacts('*_dsl_script.tar.gz')
        }
    }
}


