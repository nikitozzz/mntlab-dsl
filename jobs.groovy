job('./EPBYMINW3092/MNTLAB-akonchyts-main-build-job') {
   description 'This job is required to trigger the rest four from one place'
   parameters {
     choiceParam('BRANCH_NAME', ['akonchyts', 'master'], 'Choose appropriate branch')
   }
   scm {
       github 'MNT-Lab/mntlab-dsl', '$BRANCH_NAME'
   }
   steps {
     shell('echo "Main Job"')
   }
}

['1', '2', '3', '4'].each { suffix ->
job('./EPBYMINW3092/MNTLAB-akonchyts-child' + suffix + '-build-job') {
    description 'The job executes the cloned ‘script.sh’ from branch propagated by main job'
    steps {
      shell('echo "Child Job"')
    }
}
}
