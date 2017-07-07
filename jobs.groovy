//TODO:some stuff
def giturl = 'https://github.com/MNT-Lab/mntlab-dsl.git'
def branchname = 'vtarasiuk'

job ('example') {
    scm {
        git (giturl, branchname)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
        //shell(readFileFromWorkspace('script.sh'))
    }
}



