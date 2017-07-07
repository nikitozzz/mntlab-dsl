//TODO:some stuff
def giturl = 'https://github.com/MNT-Lab/mntlab-dsl.git'
def branchname = 'vtarasiuk'

folder('TestFolder')
def testJob = freeStyleJob('TestFolder/DSL-Task-1-Child')

job(testJob) {
    scm {
        git (giturl, branchname)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
        shell(readFileFromWorkspace('script.sh'))
    }
}



