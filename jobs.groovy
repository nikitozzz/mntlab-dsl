mavenJob('example-1') {
    postBuildSteps {
        shell("echo '1 run after Maven'")
    }
}

// run post build steps only when the build succeeds
mavenJob('example-2') {
    postBuildSteps('SUCCESS') {
        shell("echo 'run after 1-st Job'")
    }
}
