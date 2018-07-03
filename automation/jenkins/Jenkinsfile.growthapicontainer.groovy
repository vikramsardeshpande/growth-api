containerName = "growth-api-container"
container_version = "1.0.0.${env.BUILD_NUMBER}"
dockerTag = "${containerName}:${container_version}"

node("dockerlinuxspot")
{

    timestamps {
        stage("Checkout") {
            checkout scm
            sh "git clean -fdx"
        }

        stage("Build ${containerName} container") {
            dir("./automation/docker") {
                sh "docker build --no-cache -t ${dockerTag} ."
            }
        }

       # stage("Push to dev ECR east") {
       #        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'jenkins-aws-dev', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]){
        #        sh "eval `aws ecr get-login --no-include-email --region us-east-1`"
         #       sh "docker tag ${dockerTag} 239641032376.dkr.ecr.us-east-1.amazonaws.com/devops/${dockerTag}"
          #      sh "docker push 239641032376.dkr.ecr.us-east-1.amazonaws.com/devops/${dockerTag}"
           # }
       # }

        #stage("Docker Cleanup") {
        #    sh "docker images ${dockerTag} -q | tee ./xxx"
         #   sh 'docker rmi `cat ./xxx` --force ||exit 0'
        #}
    }
}
