#!/bin/bash

# Fail on any error.
set -e
# Display commands to stderr.
set -x

cd github/app-gradle-plugin

sudo /opt/google-cloud-sdk/bin/gcloud components update
sudo /opt/google-cloud-sdk/bin/gcloud components install app-engine-java

./gradlew check

# copy pom with the name expected in the Maven repository

# POSTPONE THIS FOR NOW
#######################
#ARTIFACT_ID=$(mvn -B help:evaluate -Dexpression=project.artifactId 2>/dev/null | grep -v "^\[")
#PROJECT_VERSION=$(mvn -B help:evaluate -Dexpression=project.version 2>/dev/null| grep -v "^\[")
#cp pom.xml target/${ARTIFACT_ID}-${PROJECT_VERSION}.pom

