def student_surname = "zubkov"
def jobsList = []
def firstJobIndex = 1
def lastJobIndex = 4
def command = "git ls-remote -h -t https://github.com/nikitozzz/mntlab-dsl.git"

//delete previous child jobsList
import jenkins.model.*

def matchedJobs = Jenkins.instance.items.findAll { job ->
    job.name =~ /EPRURYAW0380*/
}
    
matchedJobs.each { job ->
    println job.name
    job.delete()
}

job("EPRURYAW0380-MNTLAB-${student_surname}-main-build-job") 
	{
		def selectedBranches = command.execute().text.readLines().collect {it.split()[1].replaceAll('refs/heads/', '')}
		selectedBranches.removeAll {!(["master",student_surname].contains(it)) 
	}
  
for(i=firstJobIndex; i<lastJobIndex+1; i++)
	{ 
		parameters 
			{
				choiceParam('BRANCH_NAME',  ['zubkov'],'')
				booleanParam("EPRURYAW0380-MNTLAB-${student_surname}-child${i}-build-job", true,"")
			}

    //create downstream job
    job("EPRURYAW0380-MNTLAB-${student_surname}-child${i}-build-job") {
      scm {
        github('nikitozzz/mntlab-dsl', student_surname)
      }
      def allBranches = command.execute().text.readLines().collect {it.split()[1].replaceAll('refs/heads/', '')}
      allBranches.remove(student_surname)
      allBranches.add(0,student_surname)
      parameters {
        choiceParam('BRANCH_NAME',  allBranches,'')
      }
      steps {
        shell('chmod 777 ./script.sh; ./script.sh > output.txt')
        shell('tar -czf ${BRANCH_NAME}_dsl_script.tar.gz script.sh groovy_task.groovy' )
      }
      publishers {
       			archiveArtifacts '${BRANCH_NAME}_dsl_script.tar.gz, output.txt'
     }
    }
    jobsList << "MNTLAB-${student_surname}-child${i}-build-job"
    steps {
        downstreamParameterized {
                trigger("EPRURYAW0380-MNTLAB-${student_surname}-child${i}-build-job") {
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
