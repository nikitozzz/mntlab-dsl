//TODO:some stuff
def giturl = 'https://github.com/MNT-Lab/mntlab-dsl.git'
def branchname = 'origin/vtarasiuk'

job ('example') {
    scm {
        git (giturl, branchname)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
	out.println('Hello from a Job DSL script!')
        //shell(readFileFromWorkspace('script.sh'))
    }
}



