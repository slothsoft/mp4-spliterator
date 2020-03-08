@echo off

REM ----------------------------------------------------------------------------
REM ffmpeg -i "C:\source.mp4" -ss 00:02.000 -t 00:04.568 -c copy "C:\target.mp4"
REM ----------------------------------------------------------------------------

set startTime=%4
set endTime=%6
set targetFileName=%9
echo %startTime% - %endTime%> %targetFileName%