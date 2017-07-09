def git = "MNT-Lab/mntlab-dsl"
def repo = "amaslakou"

job('EPBYMINW1766/MNTLAB-amaslakou-main-build-job') {
    description 'Build and test the app.'
    scm {
        github(git, repo)
    }
    steps {
        gradle 'test'
    }

    (1..4).each {
        println "Job Number: ${it}"
        job("EPBYMINW1766/MNTLAB-amaslakou-child${it}-build-job") {
            scm {
                github(git, repo)
            }
            steps {
                shell("chmod +x script.sh && ./script.sh")
            }
        }
    }
}
