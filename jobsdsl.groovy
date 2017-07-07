/**
 * Created by student on 07.07.17.
 */
import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*
job('MNTLAB-aaksionkin-DSL-build-job'){
    description 'Build and test the app.'
    scm {
        github 'sheehan/job-dsl-playground'
    }
    steps {
        gradle 'test'
    }
    publishers {
        archiveJunit 'build/test-results/**/*.xml'
    }
}
['MNTLAB-aksionkin-child1-build-job',
 'MNTLAB-aksionkin-child2-build-job',
 'MNTLAB-aksionkin-child3-build-job',
 'MNTLAB-aksionkin-child4-build-job'
].each {
    freeStyleJob(it) {
        description 'Build and test the app.'
        scm {
            github 'sheehan/job-dsl-playground'
        }
        triggers {
            scm 'H/5 * * * *'
        }
        steps {
            gradle 'test'
        }
        publishers {
            archiveJunit 'build/test-results/**/*.xml'
        }
    }
}
