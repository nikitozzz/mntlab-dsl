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
                }
            }
        }
        parameters {
            //choiceParam(String parameterName, List<String> options, String description)
            choiceParam('JOB_TO_RUN', ['EPBYMINW3088/MNTLAB-aksionkin-child1-build-job',
                                       'EPBYMINW3088/MNTLAB-aksionkin-child2-build-job',
                                       'EPBYMINW3088/MNTLAB-aksionkin-child3-build-job',
                                       'EPBYMINW3088/MNTLAB-aksionkin-child4-build-job'],
                    'Choose appropriate JOB')


            gitParam('SelecttTheBranch') {
                description('branch selection')
                type('BRANCH')
                branch('~ /*')
                defaultValue('origin/aaksionkin') // empty by default
            }
        }
        //creating child jobs
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
                        branch('$SelectTheBranch')
                        triggers {
                            scm 'H/5 * * * *'
                        }
                        steps {
                            shell('chmod +x ./script.sh; ./script.sh; ./script.sh >> output.txt; tar -czvf $SelectTheBranch_dsl_script.tar.gz output.txt script.sh')
                        }
                    }
                }
            }
        }

    }



