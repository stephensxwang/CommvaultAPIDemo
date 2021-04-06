@echo off

SET CV_CLOUDPLATFORM_SERVER_PORT=80

for /f "delims=" %%i in ('dir /a-d /b cv-automation*.jar') do set file=%%i

java -jar %file% --server.port=80