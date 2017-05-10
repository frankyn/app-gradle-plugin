@echo on

cd github/app-gradle-plugin

call gcloud.cmd components update --quiet
call gcloud.cmd components install app-engine-java --quiet

rem call gradlew.bat googleJavaFormat
git config color.diff.whitespace "red reverse"
git diff --color -R
REM call gradlew.bat check
REM curl -s https://codecov.io/bash | bash

exit /b %ERRORLEVEL%
