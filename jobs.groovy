job('MNT-lab-job') {
    scm {
        git('https://github.com/MNT-Lab/mntlab-dsl.git')
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
        shell(echo "Hello there!")
    }
}
