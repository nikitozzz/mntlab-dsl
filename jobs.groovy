/**
 * Created by student on 07.07.17.
 */
import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

job('EPBYMINW3088/MNTLAB-aaksionkin-DSL-build-job') {
    description 'Build and test the app.'
    parameters {
        scm {
            git {
                remote {
                    name('origin')
                    url('https://github.com/MNT-Lab/mntlab-dsl.git')
                }
                triggers {
                    scm 'H/5 * * * *'
                }
                steps {
                    shell(readFileFromWorkspace('script.sh'))
                }
            }
        }

        //(String parameterName, String jobToRun, String description, String filter)

        runParam('child1-build-job','EPBYMINW3088/MNTLAB-aksionkin-child1-build-job','1st child job', 'ALL')

        gitParam('BRANCH') {
            description('branch selection')
            type('BRANCH')
        }
        ['EPBYMINW3088/MNTLAB-aksionkin-child1-build-job',
         'EPBYMINW3088/MNTLAB-aksionkin-child2-build-job',
         'EPBYMINW3088/MNTLAB-aksionkin-child3-build-job',
         'EPBYMINW3088/MNTLAB-aksionkin-child4-build-job'
        ].each {
            freeStyleJob(it) {
                description 'Echo the shell.sh.'
                scm {
                    git {
                        remote {
                            name('origin')
                            url('https://github.com/MNT-Lab/mntlab-dsl.git')
                        }
                        branch('aaksionkin')
                        triggers {
                            scm 'H/5 * * * *'
                        }
                        steps {
                            shell(readFileFromWorkspace('script.sh'))
                        }
                    }
                }
            }
        }
    }
}

