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
            string(name: 'bucket-name', defaultValue: '', description: 'Enter the bucket name that you want to create')
            
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
                buildEnv.inside {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'jenkins-aws-dev', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]){
                sh 'aws s3api create-bucket --bucket ${params.bucket-name} --region us-east-1'
                }
                }
             }
        }

     }
}
