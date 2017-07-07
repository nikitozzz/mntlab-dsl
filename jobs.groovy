freeStyleJob("MNTLAB-asemirski-main-build-job") {
 scm {
        git("git://github.com/quidryan/aws-sdk-test.git")
    }
    triggers {
        scm('H/15 * * * *')
    }
}
