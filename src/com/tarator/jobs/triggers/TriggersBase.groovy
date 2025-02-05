package com.tarator.jobs

class TriggersBase {

    public def triggs
    public def context
    public String pipelineName

    public final static def TRIGGER_CRON_SONARQUBE = 'H 2 * * *'
    public final static def TRIGGER_CRON_BACKDUCK = 'H 3 * * *'

    TriggersBase(context, String pipelineName) {
        this.context = context
        this.pipelineName = pipelineName
        this.triggs = []
    }

    void addCronTrigger(String cron = '') {
        if(!PipelineNames.devPrefix) {
            if (cron) {
                triggs.add(context.cron(spec: cron))
            } else if (pipelineName.contains(PipelineNames.SONARQUBE_EXTENTION)) {
                triggs.add(context.cron(spec: TRIGGER_CRON_SONARQUBE))
            } else if (pipelineName.contains(PipelineNames.BLACKDUCK_EXTENTION)) {
                triggs.add(context.cron(spec: TRIGGER_CRON_BACKDUCK))
            } else {
                return
            }
            addTriggersToPipelineContext()
        }
    }

    void addGitHubTrigger(Map args = [:]) {
    if (!PipelineNames.devPrefix && !pipelineName.contains(PipelineNames.BLACKDUCK_EXTENTION) && !pipelineName.contains(PipelineNames.SONARQUBE_EXTENTION)) {
        if (args.getOrDefault('triggerOnPush', true)) {
            triggs.add(context.githubPush())
        }
        if (args.getOrDefault('triggerOnPullRequest', false)) {
            triggs.add(
                context.pullRequest(
                    org: args.getOrDefault('org', 'your-github-org'),
                    repo: args.getOrDefault('repo', 'your-repo'),
                    cron: args.getOrDefault('cron', 'H/5 * * * *'),
                    triggerPhrase: args.getOrDefault('triggerPhrase', '.*test this please.*'),
                    onlyTriggerPhrase: args.getOrDefault('onlyTriggerPhrase', false),
                    useGitHubHooks: args.getOrDefault('useGitHubHooks', true),
                    permitAll: args.getOrDefault('permitAll', true),
                    extensions: []
                )
            )
        }
        if (args.getOrDefault('triggerOnTag', false)) {
            triggs.add(context.githubTagPush())
        }
        addTriggersToPipelineContext()
    }
}

    void addUpstreamTrigger(Map args = [:]) {
        if(!PipelineNames.devPrefix) {
            triggs.add(
                context.upstream(
                    upstreamProjects: args.getOrDefault('upstreamProjects', 'other'),
                    threshold: args.getOrDefault('threshold', 'UNSTABLE')
                )
            )
            addTriggersToPipelineContext()
        }
    }

    void addTriggersToPipelineContext() {
        def params = ParametersBase.getParameters(context.env.JOB_NAME)
        if (params) {
            context.properties(
                [
                    context.parameters(params),
                    context.pipelineTriggers(triggs)
                ]
            )
        } else {
            context.properties([context.pipelineTriggers(triggs)])
        }
    }

    def getTriggers() {
        return this.triggs
    }

    public static def getTriggers(jobName) {
        def job = Jenkins.instance.getItemByFullName(jobName)
        def triggersList = []
        if (job) {
            def triggers = job.getTriggers()
            triggers.each { triggerEntry ->
                triggersList.add(triggerEntry.value)
            }
        }
        return triggersList
    }

    private def getStringSecret(String credID = '') {
        def jenkins = Jenkins.instance
        def creds = CredentialsProvider.lookupCredentials(JenkinsCredentials.class, jenkins, null, null)
        String secretText = ''

        creds.each { c ->
            if ((c instanceof StringCredentialsImpl || c instanceof AzureSecretStringCredentials) && c.id.trim() == credID.trim()) {
                secretText = c.secret.plainText
            }
        }
        
        return secretText
    }
}