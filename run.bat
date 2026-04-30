@echo off
chcp 65001 > nul
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -jar build\libs\TextFighter.jar
pause
