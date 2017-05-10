#!/bin/bash

# Fail on any error.
set -e
# Display commands to stderr.
set -x

cd github/app-gradle-plugin
gcloud components update
gcloud components install app-engine-java
./gradlew check
# bash <(curl -s https://codecov.io/bash)
