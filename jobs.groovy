job('EPBYMINW2466/MNTLAB-{akarzhou}-main-build-job') {
    scm {
        git {
            remote {
                name('rAK')
                url('https://github.com/MNT-Lab/mntlab-dsl.git')
            }
            branches('akarzhou', 'master')
            
        }
    }
<<<<<<< HEAD
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
=======
>>>>>>> b092f5045c60c3b71950aa7a97dfb25522b55869
}
