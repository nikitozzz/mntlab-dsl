def gitrepo = 'MNT-Lab/mntlab-dsl'
def branchname = 'vtarasiuk'
job ('EPBYMINW2471/MNTLAB-vtarasiuk-child1-build-job') {
    scm {
        github (gitrepo, branchname)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
    	shell('./script.sh')
    }
}
job ('EPBYMINW2471/MNTLAB-vtarasiuk-child2-build-job') {
    scm {
        github (gitrepo, branchname)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
    	shell('./script.sh')
    }
}
job ('EPBYMINW2471/MNTLAB-vtarasiuk-child3-build-job') {
    scm {
        github (gitrepo, branchname)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
    	shell('./script.sh')
    }
}
job ('EPBYMINW2471/MNTLAB-vtarasiuk-child4-build-job') {
    scm {
        github (gitrepo, branchname)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
    	shell('./script.sh')
    }
}



