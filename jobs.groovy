job('asemirski-main-job') {
	description 'master job.'
	scm {
        github 'sheehan/job-dsl-playground'
    }
    steps {
        gradle 'test'
    }
    publishers {
        archiveJunit 'build/test-results/**/*.xml'
    }
}
        
