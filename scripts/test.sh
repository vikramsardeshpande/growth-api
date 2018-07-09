#!/bin/bash  
aws s3api create-bucket --bucket ${params.bucket-name} --region us-east-1
