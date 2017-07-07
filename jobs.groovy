//Yuri Shchanouski
def git = "https://github.com/MNT-Lab/mntlab-dsl.git"
def repo = "yshchanouski"
job('EPBYMINW2468/MNTLAB-yshchanouski-main-build-job') {
    scm {
        github(git, repo)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
	println('Hello from a Job DSL script!')        
        shell('script.sh')
    }
}

1.upto(4) {
job('EPBYMINW2468/MNTLAB-yshchanouski-child${it}-build-job') {
    scm {
        github(git, repo)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
	println('Hello from a Job DSL script!')        
        shell('script.sh')
    }
}

}
