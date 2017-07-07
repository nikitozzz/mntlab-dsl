job('MNTLAB-{akarzhou}-main-build-job') {
    scm {
        git {
            remote {
                name('remoteB')
                url('https://github.com/MNT-Lab/mntlab-dsl.git')
            }
            branches('akarzhou', 'master')
            
        }
    }
  parameters {
     choiceParam('BRANCH_NAME', ['akarzhou', 'master'], 'Choose appropriate branch')
} 	
}
  
['1', '2', '3', '4'].each { suffix ->
job('MNTLAB-{akarzhou}-child' + suffix + '-build-job') {
steps {
shell('echo "Hello world"')
}
}
}
