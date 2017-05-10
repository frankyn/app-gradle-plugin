@echo on

cd github/app-gradle-plugin

rem call gcloud.cmd components update --quiet
rem call gcloud.cmd components install app-engine-java --quiet

call gradlew.bat googleJavaFormat
git config color.diff.whitespace "red reverse"
git diff --color -R --ws-error-highlight=all
REM call gradlew.bat check
REM curl -s https://codecov.io/bash | bash

exit /b %ERRORLEVEL%
