job('EPBYMINW2466/MNTLAB-{akarzhou}-main-build-job') {
    scm {
        git {
            remote {
                name('rAK')
                url('https://github.com/MNT-Lab/mntlab-dsl.git')
            }
            branches('akarzhou', 'master')
            extensions {
                choosingStrategy {
                    alternative()
                }
            }
        }
    }
}
