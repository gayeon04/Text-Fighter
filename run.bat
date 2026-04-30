@echo off
chcp 65001 > nul
title Text-Fighter

echo [Text-Fighter] Building and running...
echo.

call gradlew.bat run
if %ERRORLEVEL% neq 0 (
    echo.
    echo [Error] Build or run failed. Exit code: %ERRORLEVEL%
    pause
    exit /b %ERRORLEVEL%
)

pause
