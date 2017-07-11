def student = "vulantsau"
def jobList = []
def firstJobIndex = 1
def lastJobIndex = 4
def command = "git ls-remote -h https://github.com/MNT-Lab/mntlab-dsl.git"

job("EPBYMINW2470/MNTLAB-${student}-main-build-job") {
  
  def selectedBranches = command.execute().text.readLines().collect {it.split()[1].replaceAll('refs/heads/', '')}
  selectedBranches.removeAll {!(["master",student].contains(it)) }
  
  for(i=firstJobIndex; i<lastJobIndex+1; i++){
    
    parameters {
      choiceParam('BRANCH_NAME',  selectedBranches,'')
      booleanParam("EPBYMINW2470/MNTLAB-${student}-child${i}-build-job", true,"")
    }

    //create downstream job
    job("EPBYMINW2470/MNTLAB-${student}-child${i}-build-job") {
      scm {
        github('MNT-Lab/mntlab-dsl', student)
      }
      def allBranches = command.execute().text.readLines().collect {it.split()[1].replaceAll('refs/heads/', '')}
      allBranches.remove(student)
      allBranches.add(0,student)
      parameters {
        choiceParam('BRANCH_NAME',  allBranches,'')
      }
      steps {
        shell('chmod 777 ./script.sh; ./script.sh > output.log')
        shell('tar -czf ${BRANCH_NAME}_dsl_script.tar.gz script.sh jobs.groovy' )
      }
      publishers {
       			archiveArtifacts '${BRANCH_NAME}_dsl_script.tar.gz, output.txt'
     }
    }
    jobList << "MNTLAB-${student}-child${i}-build-job"
    steps {
        downstreamParameterized {
                trigger("EPBYMINW2470/MNTLAB-${student}-child${i}-build-job") {
                        block{
                                buildStepFailure('FAILURE')
                                failure('FAILURE')
                                unstable('UNSTABLE')
                        }
                        parameters {
                                predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                        }
                }
        }    
    }
  }
}
  
