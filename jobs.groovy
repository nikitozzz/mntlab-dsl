def gitrepo = 'MNT-Lab/mntlab-dsl'
def branchname = 'vtarasiuk'

delete (EPBYMINW2471/MNTLAB-vtarasiuk-child1-build-job)

for (i in 1 .. 4) {
    job("EPBYMINW2471/MNTLAB-vtarasiuk-child$i-build-job") {
        scm {
            github(gitrepo, branchname)
        }
        triggers {
            scm('H/5 * * * *')
        }
        steps {
            shell('chmod +x ./script.sh')
            shell('./script.sh')
        }
    }
}
job("EPBYMINW2471/MNTLAB-vtarasiuk-main-build-job") {
    scm {
        github(gitrepo, branchname)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
        shell('chmod +x ./script.sh')
        shell('./script.sh')
    }
}



