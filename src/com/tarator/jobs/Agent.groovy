package com.tarator.jobs

class Agent {

    static final String K8S_TARATOR = 'tarator.custom.domain.com'
    static final def getClouds(def agents = []) {
        if (agents) {
            return agents
        }
        return [K8S_TARATOR]
    }

    static String getKubernetesAgentYaml(Map args = [:]) {
        Boolean helmKubectl = args.getOrDefault('helmKubectl', false)
        Boolean docker = args.getOrDefault('docker', false)

        String yaml = """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnlp
    image: jenkins/inbound-agent
    imagePullPolicy: Always
    args: ['-disableHttpsCertValidation']
"""

        if(docker) {
           yaml += '''\
  - name: docker
    image: docker:dind
    imagePullPolicy: Always
    securityContext:
      privileged: true
'''
        }

        if (helmKubectl) {
            yaml += '''\
  - name: helm-kubectl
    image: dtzar/helm-kubectl
    imagePullPolicy: Always
    command:
    - cat
    tty: true
'''
        }

        return yaml
    }

}
