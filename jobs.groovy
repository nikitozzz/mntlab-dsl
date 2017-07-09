freeStyleJob('EPBYMINW6405/MNTLAB-pyurchuk-main-build-job'){
    description 'Building necessary jobs'

scm {
    git {
     github 'MNT-Lab/mntlab-dsl', '$BRANCH_NAME'
        }
        branches('pyurchuk', 'master')                    
    }      

parameters {
     choiceParam('BRANCH_NAME', ['pyurchuk', 'master'], 'Choose appropriate branch')
	extendedChoiceParameterDefinition {
          name('Select job')
          type('multiselect')
      visibleItemCount(4)
          value('EPBYMINW6405/MNTLAB-pyurchuk-child1-build-job, EPBYMINW6405/MNTLAB-pyurchuk-child2-build-job, EPBYMINW6405/MNTLAB-pyurchuk-child3-build-job, EPBYMINW6405/MNTLAB-pyurchuk-child4-build-job')
      multiSelectDelimiter(',')
          }
      }

publishers {
        archiveArtifacts {
            pattern('script.sh')
            onlyIfSuccessful()
        }
}   

def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
def command = "git ls-remote -h $gitURL"  

['1', '2', '3', '4'].each { suffix ->
job('EPBYMINW6405/MNTLAB-pyurchuk-child' + suffix + '-build-job') {
  parameters {
  choiceParam('BRANCH_NAME', branches)
}

steps {
    copyArtifacts('EPBYMINW6405/MNTLAB-pyurchuk-main-build-job') {
            includePatterns('script.sh')
            targetDirectory('./')
            flatten()
            optional()
            buildSelector {
                latestSuccessful(true)
            }
    }
}

steps {
    shell('chmod +x ./script.sh; ./script.sh > output.txt; tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt')
}

publishers {
        archiveArtifacts {
        	pattern('output.txt')
  	     	pattern('${BRANCH_NAME}_dsl_script.tar.gz')
            onlyIfSuccessful()
            }
        }
    }
}
}
