def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
def command = "git ls-remote -h -t $gitURL"
def proc = command.execute()
def branches = proc.in.text.readLines().collect
        {
            it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
        }   
job('MNTLAB-mdemenkova-main-build-job'){
    scm {
        github ('MNT-Lab/mntlab-dsl', '$BRANCH_NAME')
     }      
   
    parameters {
        choiceParam('BRANCH_NAME', ['mdemenkova', 'master'], 'select branch')
    }
  publishers {
    downstream ('MNTLAB-mdemenkova-child1-build-job', 'SUCCESS')
    downstream ('MNTLAB-mdemenkova-child2-build-job', 'SUCCESS')
    downstream ('MNTLAB-mdemenkova-child3-build-job', 'SUCCESS')
    downstream ('MNTLAB-mdemenkova-child4-build-job', 'SUCCESS')
  }
  	
}


//describe all branches for manual start childjobs
def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
def command = "git ls-remote -h -t $gitURL"
def proc = command.execute()
def branches = proc.in.text.readLines().collect
        {
            it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
        }

//describe childjobs
['MNTLAB-mdemenkova-child1-build-job', 'MNTLAB-mdemenkova-child2-build-job', 'MNTLAB-mdemenkova-child3-build-job', 'MNTLAB-mdemenkova-child4-build-job'].each {
    job(it){
    parameters {
        choiceParam('BRANCH_NAME', branches)
    }		   
    scm {
         github ('MNT-Lab/mntlab-dsl', '$BRANCH_NAME')
    }
    steps {
        shell('chmod +x ./script.sh; ./script.sh >> output.txt; tar -czvf $BRANCH_NAME_dsl_script.tar.gz output.txt script.sh')
    	
 }
      
    }
}
               }
