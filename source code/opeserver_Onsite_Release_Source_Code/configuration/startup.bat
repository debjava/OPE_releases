@echo off
echo.*********************************************************************************
echo.*										*
echo.*  Starting the OPE FTP server						*
echo.*										*
echo.*  Developed by Ideal Invent Technology, Bangalore				*
echo.*										*
echo.*										*
echo.*********************************************************************************
echo.

REM java -jar opeserver.jar

java -jar ./lib/opeserver.jar

if "%NOPAUSE%" == "" pause

