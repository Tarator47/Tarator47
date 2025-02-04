package com.tarator.jobs

class Agent {

    static final String K8S_TARATOR = 'tarator.custom.domain.com'

    static String getKubernetesAgentYaml(Map args) {
        String image = args.getOrDefault('image', 'jenkins-inbound-agent:3261.v9c670a_4748a_9-2-jdk17-RA')
        Boolean helmKubectl = args.getOrDefault('helmKubectl', false)
        Boolean docker = args.getOrDefault('docker', false)

        String yaml = """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnlp
    image: cvbartifactorykce.ra-int.com/infosoft-registry/${image}
    imagePullPolicy: Always
    args: ['-disableHttpsCertValidation']
"""

        if(docker) {
           yaml += '''\
  - name: docker
    image: cvbartifactorykce.ra-int.com/infosoft-registry-local/docker:26.1.3-dind-RA
    imagePullPolicy: Always
    securityContext:
      privileged: true
'''
        }

        if (helmKubectl) {
            yaml += '''\
  - name: helm-kubectl
    image: cvbartifactorykce.ra-int.com/infosoft-registry/helm-kubectl-python:1.00.00.006
    imagePullPolicy: Always
    command:
    - cat
    tty: true
'''
        }

        return yaml
    }

}
