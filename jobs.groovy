
job('MNTLAB-amaslakou-main-build-job'){

    define "Main job"
    scm{
        github "MNT-Lab/mntlab-dsl", "amaslakou"
    }
    steps {
        gradle 'test'
    }
}