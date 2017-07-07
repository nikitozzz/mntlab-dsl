job('MNTLAB-akonchyts-main-build-job') {
   description 'This job is required to trigger the rest four from one place'
   steps {
     shell('echo "Main Job"')
   }
}

['1', '2', '3', '4'].each { suffix ->
job('MNTLAB-akonchyts-child' + suffix + '-build-job') {
    description 'The job executes the cloned ‘script.sh’ from branch propagated by main job'
    scm {
        github 'sheehan/job-dsl-playground'
    }
    steps {
      shell('echo "Child Job"')
    }
}
}
