freestylejob('EPBYMINW6405/MNTLAB-pyurchuk-main-build-job'){
  
  description 'Building necessary jobs'
  
  steps {
        shell ('echo "The job is done! Take some beer."')
    }
}

['EPBYMINW6405/MNTLAB-pyurchuk-child1-build-job',
 'EPBYMINW6405/MNTLAB-pyurchuk-child2-build-job',
 'EPBYMINW6405/MNTLAB-pyurchuk-child3-build-job',
 'EPBYMINW6405/MNTLAB-pyurchuk-child4-build-job'
].each {
    freeStyleJob(it) {
        steps {
            shell ('echo "Hello there!"')
        }
	}
}
