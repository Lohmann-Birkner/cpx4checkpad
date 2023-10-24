REM JUST FOR DEMONSTRATION (ADAPT THIS FILE FOR YOU REQUIREMENTS!)
@echo off
for /F "usebackq tokens=1,2 delims==" %%i in (`wmic os get LocalDateTime /VALUE 2^>NUL`) do if '.%%i.'=='.LocalDateTime.' set ldt=%%j
set ldt=%ldt:~0,4%-%ldt:~4,2%-%ldt:~6,2% %ldt:~8,2%-%ldt:~10,2%-%ldt:~12,6%
set CPX_HOME=C:\workspace\CheckpointX_Repository\trunk\WD_CPX_Server
set logFile=%CPX_HOME%\sap_import\log\sap-import-%ldt%.log
echo Log File will be %logFile%
cd ..
java -cp %CPX_HOME%\libaries\sapjco3.jar;%CPX_HOME%\sap_import\sqljdbc41.jar;%CPX_HOME%\sap_import\cpx-sap-importer-1.0-SNAPSHOT.jar de.lb.cpx.sap.importer.SapImportProcessTest 0001 260100056 dbsys1:SAP_TEST 2> "%logFile%"	
REM PAUSE