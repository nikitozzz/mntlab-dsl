freeStyleJob("DSL-Tutorial-1-Test") {
 scm {
        git("git://github.com/quidryan/aws-sdk-test.git")
    }
    triggers {
        scm('H/15 * * * *')
    }
}
