import com.rockwellautomation.gui.PipelineNames
import hudson.EnvVars

String pipelineUrl = "https://github.com/Tarator47/Tarator47"
String pipelineBranchMain = branchMain
def daysKeep = 30
def artifactDaysKeep = 7

categorizedJobsView(PipelineNames.jobFolderCPG + '/11 latest') {
    jobs {
        regex("${PipelineNames.devPrefix}${PipelineNames.devPrefix != "" ? "-" : ""}.*")
    }
    categorizationCriteria {
        regexGroupingRule('Build-.*', 'Build')
        regexGroupingRule('Integration-.*', 'Integration')
    }
    columns {
        status()
        weather()
        categorizedJob()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

[
    // Build 
    [jobName:  PipelineNames.BUILD_TEST_PIPELINE, scriptPath: 'Jenkinsfiles-Sudoku/Build-Test/Jenkinsfile', url: pipelineUrl, branch: branchMain, disabled: false],
    // Integration 
    [jobName:  PipelineNames.INTEGRATION_TEST_PIPELINE, scriptPath: 'Jenkinsfiles-Sudoku/IntegratonTest/Jenkinsfile', url: pipelineUrl, branch: branchMain, disabled: false]
].each { Map config ->
    pipelineJob(config.jobName) {
        disabled(config.disabled)
        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url(config.url)
                            credentials() // TRQBWA CRED
                        }
                        branch(config.branch)
                    }
                    scriptPath(config.scriptPath)
                }
                lightweight(true)
            }
        }
        logRotator {
            daysToKeep(daysKeep)
            artifactDaysToKeep(artifactDaysKeep)
        }
    }
}
