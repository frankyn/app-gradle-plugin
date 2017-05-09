@echo on

cd github/app-gradle-plugin

call gcloud.cmd components update --quiet
call gcloud.cmd components install app-engine-java --quiet

gradlew.bat googleJavaFormat
git diff
REM gradlew.bat check
REM curl -s https://codecov.io/bash | bash

exit /b %ERRORLEVEL%
