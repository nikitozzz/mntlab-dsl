def git = "MNT-Lab/mntlab-dsl"
def repo = "ndolya"
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

job("EPBYMINW1969/MNTLAB-$repo-main-build-job") {
    parameters {
	choiceParam("BRANCH_NAME", branches)
    }
    scm {
        github(git, repo)
    }
    triggers {
        scm('H/5 * * * *')
    }
}

1.upto(4) {
  job("EPBYMINW1969/MNTLAB-$repo-child${it}-build-job") {
    scm {
        github(git, repo)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
        shell('chmod +x script.sh && ./script.sh')
    }
}

}


