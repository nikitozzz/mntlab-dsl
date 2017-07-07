job('gr8 example') {
   scm {
       github 'sheehan/job-dsl-gradle-example' 
   }
   triggers { 
       scm 'H/5 * * * *' 
   } 
   steps { 
        gradle 'clean test' 
   } 
   publishers {
       archiveJunit 'build/test-results/**/*.xml' 
       extendedEmail 'mr.sheehan@gmail.com' 
   } 
}
