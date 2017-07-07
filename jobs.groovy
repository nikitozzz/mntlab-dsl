import jenkins.*
import jenkins.model.*
  
job('Create MNTLAB jobs'){
    scm {
        github 'https://github.com/MNT-Lab/mntlab-dsl.git'
    }
    steps {
        shell ('echo "Hello there!"')
    }
}
['MNTLAB-pyurchuk-child1-build-job',
 'MNTLAB-pyurchuk-child2-build-job',
 'MNTLAB-pyurchuk-child3-build-job',
 'MNTLAB-pyurchuk-child4-build-job'
].each {
    freeStyleJob(it) {
        steps {
            shell ('echo "Hello there!"')
        }
	}
}
