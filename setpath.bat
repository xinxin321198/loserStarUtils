@rem @for /F "delims=" %%I in ("%~dp0") do echo %%~fI
@echo off
chcp 936
color a
echo ----------running maven start----------------------
rem @setlocal
set JAVA_HOME=e:\jdk1.8
set classpath=.;.\classes\;.\target\test-classes\;.\target\classes\;%JAVA_HOME%\jre\lib\rt.jar;%JAVA_HOME%\lib\tools.jar;
set LANG=zh_CN.UTF8
set path=.;%MAVEN_HOME%;%JAVA_HOME%\bin;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem
set MAVEN_HOME=E:\myIDE\maven-3.3.9\bin
call mvn -v
echo ------------end--------------
