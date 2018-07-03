# Container for jobs
FROM microsoft/powershell:ubuntu-16.04

# Install packages
#RUN apt-get update
#RUN apt-get -y install wget python-pip python-dev libyaml-dev zip jq postgresql-client redis-tools
#RUN pip install awscli pandas redis
RUN export PATH=$PATH:/usr/local/bin

# Setup SSL
RUN mkdir -p /etc/pki/tls/certs
RUN ln -s /usr/lib/ssl/certs/ca-certificates.crt /etc/pki/tls/certs/ca-bundle.crt
