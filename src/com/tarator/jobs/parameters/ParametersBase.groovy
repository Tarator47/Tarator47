package com.tarator.jobs.parameters

import com.tarator.gui.JobParameters
import com.tarator.gui.PipelineNames
import com.tarator.jobs.Agent


import hudson.model.*
import hudson.model.ParametersDefinitionProperty
import hudson.util.DescribableList
import jenkins.model.*
import org.jenkinsci.plugins.workflow.job.*

class ParametersBase {

    public def context
    public def params
    public String pipelineName

    public static final String PARAM_BASE_AGENT_LABEL = 'AGENT_LABEL'
    public static final String PARAM_BASE_JUST_UPDATE_JOB_CONFIG = 'JUST_UPDATE_JOB_CONFIG'

    ParametersBase(context, String pipelineName) {
        this.context = context
        this.params = []
        this.pipelineName = pipelineName
    }

    String validate(String errorMsg = '') {
        // check that a required parameter is not empty
        for (param in params) {
            if (param.toMap()['description'] != null && param.toMap()['description'].startsWith(JobParameters.PARAM_REQUIRED) && context.params[param.toMap()['name']].equals('')) {
                errorMsg += "\nParameter '${param.toMap()['name']}' is required"
            }
        }

        if (errorMsg != '') {
            context.error errorMsg
            context.println errorMsg
            context.currentBuild.result = 'FAILURE'
        } else {
            context.currentBuild.result = 'SUCCESS'
        }

        return errorMsg
    }

    public void addParamsToPipelineContext() {
        context.properties([context.parameters(params)])
    }

    def getParameters() {
        return this.params
    }

    public static def getParameters(jobName) {
        def job = Jenkins.instance.getItemByFullName(jobName)
        def paramsList = []
        if (job) {
            def parameters = job.getProperty(ParametersDefinitionProperty.class)?.parameterDefinitions
            parameters?.each { param ->
                paramsList.add(param)
            }
        }
        return paramsList
    }

    protected void addJobSettingsParams(def agents = []) {
        params.add(context.separator(name: 'JOB_SETTINGS', sectionHeader: 'Job Settings', separatorStyle: JobParameters.SEPARATOR_STYLE, sectionHeaderStyle: JobParameters.SECTION_HEADER_STYLE))
        params.add(context.choice(name: PARAM_BASE_AGENT_LABEL, choices: Agent.getClouds(agents), description: "${JobParameters.PARAM_OPTIONAL} Agent/Cloud which will be used for builds."))
        params.add(context.booleanParam(name: PARAM_BASE_JUST_UPDATE_JOB_CONFIG, defaultValue: false, description: "${JobParameters.PARAM_OPTIONAL} Just Update the configuration of this job - exit on Configuration step"))
    }

    protected void removeParam(String paramName) {
        params.each { param ->
            if (param.getArguments().name.equals(paramName)) {
                context.println "remove parameter '${paramName}'"
                params.remove(param)
            }
        }
    }
}
