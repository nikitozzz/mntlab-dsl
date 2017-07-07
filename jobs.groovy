def gitrepo = 'MNT-Lab/mntlab-dsl'
def branchname = 'vtarasiuk'
def branchApi = new URL("https://api.github.com/repos/${gitrepo}/branches")
def branches = new groovy.json.JsonSlurper().parse(branchApi.newReader())
def BRANCH_NAME = branches.name

for (i in 1 .. 4) {
    job("EPBYMINW2471/MNTLAB-vtarasiuk-child$i-build-job") {
        parameters {
            stringParam('MESSAGE', '-->Param-message <--!')
        }
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



