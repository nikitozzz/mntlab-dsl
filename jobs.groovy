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
    for (i = 1; i <5; i++) {  
    downstream ("MNTLAB-mdemenkova-child${i}-build-job", 'SUCCESS') 
  }
  }
  	
}



def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
def command = "git ls-remote -h -t $gitURL"
def proc = command.execute()
def branches = proc.in.text.readLines().collect
        {
            it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
        }


for (i = 1; i <5; i++) {
  job ("MNTLAB-mdemenkova-child${i}-build-job"){ 
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
               
