pipeline {
    agent any

      options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10', daysToKeepStr: '30',))
         }

      environment {
        pullAccount = "294074132694"
        containerName = "growth-api-container"
        growthapipartner_container_version = "1.0.0.12"
        pullCredName = "jenkins-aws-dev"

    }

        parameters {
            choice(name: 'account', choices: 'development\npipeline\nproduction', description: '')
            string(name: 'zone', defaultValue: '', description: '')
            string(name: 'partnerName', defaultValue: 'TestPartner', description: '')
            string(name: 'partnerRenaissanceId', defaultValue: '', description: '')
            string(name: 'tenants', defaultValue: '', description: '')

        }

    stages {
        stage ('Checkout') {
            steps {

                checkout scm
                sh 'git clean -fdx > /dev/null'
            }
         }

        stage ('Pull ECR Image') {

            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: "${pullCredName}", accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']])
            {
                sh 'eval `aws ecr get-login --no-include-email --region us-east-1`'
                sh 'docker pull ${pullAccount}.dkr.ecr.us-east-1.amazonaws.com/devops/growth-api-container:${growthapipartner_container_version}'
            }
            script {
                buildEnv = docker.image("${pullAccount}.dkr.ecr.us-east-1.amazonaws.com/devops/growth-api-container:${growthapipartner_container_version}")
            }
         }
        }

        stage ('Run Script') {

            steps {
              script {

                buildEnv.inside {

                    sh '/bin/bash scripts/test.sh'
                    
                }
              }
            }
        }

     }
}
