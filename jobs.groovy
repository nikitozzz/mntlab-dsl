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
/** Setting list of master  (hardcode)*/
String student = 'vtarasiuk'; String master = 'master'
def masterchoice = [student, master]
/** Setting list of job names  (hardcode)*/
for (i in 1..4){
    def jobnames =+ [MNTLAB-vtarasiuk-child$i-build-job]
}
/** Create child jobs*/
jobnames.each {
    job("EPBYMINW2471/$it") {
        parameters {
            choiceParam('BRANCH_NAME', branches)
        }
        scm {
            github(gitrepo, branchname)
        }
        triggers {
            scm('H/5 * * * *')
        }
        steps {

            shell ('chmod +x script.sh && ./script.sh > output.txt && cat output.txt')
            shell ('tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt jobs.groovy script.sh')
        }
        publishers {
            archiveArtifacts('output.txt')
            archiveArtifacts('*_dsl_script.tar.gz')
        }
    }
}

/** Create Master job*/
job("EPBYMINW2471/MNTLAB-vtarasiuk-main-build-job") {
    parameters {
        choiceParam('BRANCH_NAME', masterchoice)
        runParam(jobnames)
    }
    scm {
        github(gitrepo, branchname)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
        shell('chmod +x script.sh && ./script.sh > output.txt && cat output.txt')
    }
}



