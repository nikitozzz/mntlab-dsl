//TODO:some stuff
def gitrepo = 'MNT-Lab/mntlab-dsl'
def branchname = 'vtarasiuk'

job ('example') {
    scm {
        github (gitrepo, branchname)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
	out.println('Hello from a Job DSL script!')
        //shell(readFileFromWorkspace('script.sh'))
    }
}



