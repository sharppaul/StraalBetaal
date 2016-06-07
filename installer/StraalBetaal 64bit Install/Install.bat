@echo off

:CheckOS
IF EXIST "%PROGRAMFILES(X86)%" (GOTO 64BIT) ELSE (GOTO 32BIT)

:64BIT
echo # Installing StraalBetaal Client...


echo #
echo # For this program to install and work Java is needed. 
echo # If the registry keys are not found please install Java from www.java.com.

REM ##########################  Check Java: ###########################

FOR /F "skip=2 tokens=2*" %%A IN ('REG QUERY "HKLM\Software\JavaSoft\Java Runtime Environment" /v CurrentVersion') DO set CurVer=%%B
FOR /F "skip=2 tokens=2*" %%A IN ('REG QUERY "HKLM\Software\JavaSoft\Java Runtime Environment\%CurVer%" /v JavaHome') DO set JAVA_HOME=%%B
IF [%JAVA_HOME] == [] GOTO NOJAVA

echo # 
echo # Java Version:	 %CurVer%
echo # Java Path:	 %JAVA_HOME%


echo #
echo #

  
REM  ##########################  Copy files: ###########################

echo # Copying files...
if not exist "%PROGRAMFILES%\StraalBetaal" mkdir "%PROGRAMFILES%\StraalBetaal"
copy "%~dp0client\Client.jar" "%PROGRAMFILES%\StraalBetaal\Client.jar"
copy "%~dp0client\logo.ico" "%PROGRAMFILES%\StraalBetaal\logo.ico"
copy "%~dp0rxtx\rxtxParallel.dll" "%JAVA_HOME%\bin\rxtxParallel.dll"
copy "%~dp0rxtx\rxtxSerial.dll" "%JAVA_HOME%\bin\rxtxSerial.dll"
copy "%~dp0rxtx\RXTXcomm.jar" "%JAVA_HOME%\lib\ext\RXTXcomm.jar"


echo #
echo #


REM ##########################  Create shortcut: ###########################

set SCRIPT="%TEMP%\%RANDOM%-%RANDOM%-%RANDOM%-%RANDOM%.vbs"
echo # Creating shortcut...
echo Set oWS = WScript.CreateObject("WScript.Shell") >> %SCRIPT%
echo sLinkFile = "%USERPROFILE%\Desktop\StraalBetaal.lnk" >> %SCRIPT%
echo Set oLink = oWS.CreateShortcut(sLinkFile) >> %SCRIPT%
echo oLink.TargetPath = "%PROGRAMFILES%\StraalBetaal\Client.jar" >> %SCRIPT%
echo oLink.IconLocation = "%PROGRAMFILES%\StraalBetaal\logo.ico" >> %SCRIPT%
echo oLink.Save >> %SCRIPT%
cscript /nologo %SCRIPT%
echo %SCRIPT%
del %SCRIPT%

echo # Done installing StraalBetaal Client...
GOTO END




:32BIT
echo # The system is not running 64-bit windows. Please use the correct installer...
GOTO END




:END
pause > nul