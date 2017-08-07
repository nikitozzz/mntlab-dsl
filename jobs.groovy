def student_surname = "zubkov"
def jobsList = []
def firstJobIndex = 1
def lastJobIndex = 4
def command = "git ls-remote -h -t https://github.com/nikitozzz/mntlab-dsl.git"
println "step1"
job("EPRURYAW0380-MNTLAB-${student_surname}-main-build-job") 
	{
		def selectedBranches = command.execute().text.readLines().collect {it.split()[1].replaceAll('refs/heads/', '')
	}
		selectedBranches.removeAll 
			{
				!(["master",student_surname].contains(it)) 
			}
println "step2"		
for(i=firstJobIndex; i<lastJobIndex+1; i++)
	{ 
		parameters 
			{
				println "step3"
				choiceParam('BRANCH_NAME', selectedBranches,'')
				booleanParam("EPRURYAW0380-MNTLAB-${student_surname}-child${i}-build-job", true,"")
			}

    //create downstream job
	println "step4"
    job("EPRURYAW0380-MNTLAB-${student_surname}-child${i}-build-job") 
		{
			scm 
				{
					github('nikitozzz/mntlab-dsl', student_surname)
				}
			def allBranches = command.execute().text.readLines().collect {it.split()[1].replaceAll('refs/heads/', '')
		}
	println "step5"
	allBranches.remove(student_surname)
	allBranches.add(0,student_surname)
	parameters 
		{
			choiceParam('BRANCH_NAME',  allBranches,'')
		}
	steps 
		{
			shell('chmod 777 ./script.sh; ./script.sh > output.txt')
			shell('tar -czf ${BRANCH_NAME}_dsl_script.tar.gz script.sh groovy_task.groovy' )
		}
		publishers 
		{
			archiveArtifacts '${BRANCH_NAME}_dsl_script.tar.gz, output.txt'
		}
	}	
	println "step6"
    jobsList << "MNTLAB-${student_surname}-child${i}-build-job"
    steps 
		{
			downstreamParameterized 
				{
					println "step7"
					trigger("EPRURYAW0380-MNTLAB-${student_surname}-child${i}-build-job") 
						{
							block
								{
									println "step8"
									buildStepFailure('FAILURE')
									failure('FAILURE')
									unstable('UNSTABLE')
								}
							parameters 
								{
									println "step9"
									predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
								}
						}
				}    
		}
	}
}