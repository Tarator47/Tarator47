import hudson.model.Result
import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException

import com.tarator.build.Log

def call(justUpdateJobFlag) {
    if (justUpdateJobFlag) {
        println Log.verbose("ABORTING JOB EXECUTION", "Due to flag: JUST_UPDATE_JOB_CONFIG")
        throw new FlowInterruptedException(Result.ABORTED)
    }
}