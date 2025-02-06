package com.tarator.jobs.parameters

import com.tarator.gui.JobParameters
import com.tarator.gui.PipelineNames
import com.tarator.jobs.Agent

class ParametersBuildBase extends ParametersBase {
    protected def addGeneralBuildParams() {
        params.add(context.booleanParam(
            name: 'IS_TEST_BUILD',
            defaultValue: false,
            description: 'Mark this as a test build to prevent production deployment.'
        ))

        params.add(context.choice(
            name: 'BUILD_TYPE',
            choices: ['dev', 'release'],
            description: 'Select the type of build: Development or Release.'
        ))

        params.add(context.booleanParam(
            name: 'ENABLE_DEBUG',
            defaultValue: false,
            description: 'Enable debug mode for this build.'
        ))

        params.add(context.string(
            name: 'JIRA_ID',
            defaultValue: '',
            description: 'Jira ID for tracking this build (if applicable).'
        ))

        params.add(context.booleanParam(
            name: 'DEPLOY_DEV',
            defaultValue: false,
            description: 'Deploy the build to the development environment.'
        ))

        params.add(context.string(
            name: 'VERSION_QUALIFIER',
            defaultValue: 'v1.0.0',
            description: 'Specify a version qualifier for tagging the build.'
        ))

        params.add(context.booleanParam(
            name: 'RUN_UNIT_TESTS',
            defaultValue: true,
            description: 'Run unit tests before building.'
        ))

        params.add(context.booleanParam(
            name: 'ENABLE_LINTING',
            defaultValue: true,
            description: 'Enable code linting checks.'
        ))

        params.add(context.booleanParam(
            name: 'PUBLISH_COVERAGE',
            defaultValue: true,
            description: 'Publish code coverage reports.'
        ))

        params.add(context.string(
            name: 'DOCKER_IMAGE_TAG',
            defaultValue: 'latest',
            description: 'Tag for the Docker image.'
        ))

        params.add(context.booleanParam(
            name: 'SEND_NOTIFICATIONS',
            defaultValue: true,
            description: 'Send build and deployment notifications.'
        ))

        params.add(context.choice(
            name: 'DEPLOY_ENVIRONMENT',
            choices: ['dev', 'staging', 'production'],
            description: 'Choose the deployment environment for this build.'
        ))
    }
}