freeStyleJob('EPBYMINW6405/MNTLAB-pyurchuk-main-build-job'){
    description 'Building necessary jobs'
	}

parameters {
        choiceParam('myParameterName', 
   ['option 1 (default)', 'option 2', 'option 3'], 'my description')

['EPBYMINW6405/MNTLAB-pyurchuk-child1-build-job',
 'EPBYMINW6405/MNTLAB-pyurchuk-child2-build-job',
 'EPBYMINW6405/MNTLAB-pyurchuk-child3-build-job',
 'EPBYMINW6405/MNTLAB-pyurchuk-child4-build-job'
].each {
    freeStyleJob(it) {
    	description 'The job was created successfully'
    	}
   }

steps {
    shell('chmod +x script.sh; ./script.sh > output.log; tar -czf ${BRANCH_NAME}_dls_script.tar.gz script.sh')
	}

publishers {
    archiveArtifacts('output.log')
	}
}
