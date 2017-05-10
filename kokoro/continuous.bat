@echo on

cd github/app-gradle-plugin

rem call gcloud.cmd components update --quiet
rem call gcloud.cmd components install app-engine-java --quiet

call gradlew.bat googleJavaFormat
git config color.diff.whitespace "red reverse"
git diff --color --word-diff-regex=.
REM call gradlew.bat check
REM curl -s https://codecov.io/bash | bash

exit /b %ERRORLEVEL%
