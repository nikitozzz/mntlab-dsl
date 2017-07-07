//TODO:some stuff
def testJob = freeStyleJob('DSL-Task-1-Child')
testJob.with {
    description 'A sample of Job'
}
testJob('DSL-Task-1-Child') {
    scm {
        github 'MNT-Lab/mntlab-dsl', vtarasiuk
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
        shell(executeFileFromWorkspace('script.sh'))
    }
}
