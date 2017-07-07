freeStyleJob("DSL-Tutorial-1-Test") {
jdk('Java 8')   
 scm {
        git("git://github.com/quidryan/aws-sdk-test.git")
    }
    triggers {
        scm('H/15 * * * *')
    }
}
