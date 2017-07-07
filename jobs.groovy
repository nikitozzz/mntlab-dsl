def gitrepo = 'MNT-Lab/mntlab-dsl'
def branchname = 'vtarasiuk'
def BRANCH_NAME = branchname

def testlist = ['one', 'two']

for (i in 1 .. 4) {
    job("EPBYMINW2471/MNTLAB-vtarasiuk-child$i-build-job") {
        parameters {
            stringParam('Branch Name', testlist)
        }
        scm {
            github(gitrepo, branchname)
        }
        triggers {
            scm('H/5 * * * *')
        }
        steps {

            shell('chmod +x ./script.sh && ./script.sh')
        }
    }
}
job("EPBYMINW2471/MNTLAB-vtarasiuk-main-build-job") {
    parameters {
        stringParam('Branch Name', BRANCH_NAME)
    }
    scm {
        github(gitrepo, branchname)
    }
    triggers {
        scm('H/5 * * * *')
    }
    steps {
        shell('chmod +x ./script.sh && ./script.sh')
    }
}



