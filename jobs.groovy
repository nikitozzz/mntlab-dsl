job("EPBYMINW3093/MNTLAB-asemirski-main-build-job") {
		scm {
       			github 'MNT-Lab/mntlab-dsl', '$BRANCH_NAME'
 		 }
	       	 parameters {
   			 choiceParam('BRANCH_NAME', ['asemirski', 'master'], 'Choose branch')
  		 }
		for (i = 1; i <4; i++) {
 			 job("MNTLAB-asemirski-child${i}-build-job")
}
}
