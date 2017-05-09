@echo on

cd github/app-gradle-plugin

call gcloud.cmd components update --quiet
call gcloud.cmd components install app-engine-java --quiet

call gradlew.bat googleJavaFormat
git config color.diff.whitespace "red reverse"
git --color -R diff
REM call gradlew.bat check
REM curl -s https://codecov.io/bash | bash

exit /b %ERRORLEVEL%
