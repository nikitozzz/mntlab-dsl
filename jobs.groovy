def git = 'MNT-Lab/mntlab-dsl'
def repo = 'amaslakou'
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
        choiceParam('BRANCH_NAME', branches)
    }

    (1..4).each {
        println 'Job Number: ${it}'
        job('EPBYMINW1766/MNTLAB-amaslakou-child${it}-build-job') {
            scm {
                github(git, repo)
            }
            steps {
                shell('chmod +x script.sh && ./script.sh')
            }
        }
    }
}
