def gitrepo = 'MNT-Lab/mntlab-dsl'
def branchname = 'vtarasiuk'

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

for (i in 1 .. 4) {
    job("EPBYMINW2471/MNTLAB-vtarasiuk-child$i-build-job") {
        parameters {
            choiceParam('Branch Name', branches)
        }
        scm {
            github(gitrepo, branchname)
        }
        triggers {
            scm('H/5 * * * *')
        }
        steps {

            shell('chmod +x ./script.sh && ./script.sh')
        }
    }
}
job("EPBYMINW2471/MNTLAB-vtarasiuk-main-build-job") {
    parameters {
        stringParam('Branch Name', BRANCH_NAME)
    }
    scm {
        github(gitrepo, branchname)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
        shell('chmod +x ./script.sh && ./script.sh')
    }
}



