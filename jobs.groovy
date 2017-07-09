def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
def command = "git ls-remote -h $gitURL"
def proc = command.execute()
proc.waitFor()
if ( proc.exitValue() != 0 ) {
   println "Error, ${proc.err.text}"
   System.exit(-1)
}
def branches = proc.in.text.readLines().collect {
   it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
}

job('./EPBYMINW3092/MNTLAB-akonchyts-main-build-job') {
  description 'This job is required to trigger the rest four from one place'
  configure { project ->
        project / 'properties' / 'hudson.model.ParametersDefinitionProperty' / 'parameterDefinitions' / 'com.cwctravel.hudson.plugins.extended__choice__parameter.ExtendedChoiceParameterDefinition' {
           name('BUILDS_TRIGGER')
           description('Choose child build job(s) to run')
           quoteValue(false)
           type('PT_CHECKBOX')
           value('MNTLAB-akonchyts-child1-build-job, MNTLAB-akonchyts-child2-build-job, MNTLAB-akonchyts-child3-build-job, MNTLAB-akonchyts-child4-build-job')
           defaultValue('MNTLAB-akonchyts-child1-build-job, MNTLAB-akonchyts-child2-build-job, MNTLAB-akonchyts-child3-build-job, MNTLAB-akonchyts-child4-build-job')
           visibleItemCount(4)
           multiSelectDelimiter(',')
           projectName('MNTLAB-akonchyts-main-build-job')
       }
   }
   parameters {
     choiceParam('BRANCH_NAME', ['akonchyts', 'master'], 'Choose appropriate branch')
   }
   scm {
       github 'MNT-Lab/mntlab-dsl', '$BRANCH_NAME'
   }
   steps {
       shell('echo "Main Job"')
       downstreamParameterized {
         trigger('${BUILDS_TRIGGER}') {
            block {
                buildStepFailure('FAILURE')
                failure('FAILURE')
                unstable('UNSTABLE')
            }
            parameters {
              currentBuild()
            }
         }
       }
    }
}

['1', '2', '3', '4'].each { suffix ->
job('./EPBYMINW3092/MNTLAB-akonchyts-child' + suffix + '-build-job') {
    description 'The job executes the cloned ‘script.sh’ from branch propagated by main job'
    parameters {
        choiceParam('BRANCH_NAME', branches)
    }
    scm {
        github 'MNT-Lab/mntlab-dsl', '$BRANCH_NAME'
    }
    steps {
      shell('''chmod +x script.sh
      ./script.sh script.sh > output.txt
      cat output.txt
      if [[ -f jobs.groovy ]]
      then
      tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt script.sh jobs.groovy
      else
      tar -czf ${BRANCH_NAME}_dsl_script.tar.gz output.txt script.sh
      fi''')
    }
    publishers {
        archiveArtifacts {
            pattern('${BRANCH_NAME}_dsl_script.tar.gz')
            pattern('output.txt')
            onlyIfSuccessful()
        }
    }
}
}
