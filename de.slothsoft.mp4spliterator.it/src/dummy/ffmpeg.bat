@echo off

REM ----------------------------------------------------------------------------
REM ffmpeg -i "C:\source.mp4" -ss 00:02.000 -t 00:04.568 -c copy "C:\target.mp4"
REM ----------------------------------------------------------------------------

echo "Executing ffmpeg..."

set startTime=%4
set endTime=%6
set targetFileName=%9
echo %startTime% - %endTime%>%targetFileName%

echo "Created file: %targetFileName%"