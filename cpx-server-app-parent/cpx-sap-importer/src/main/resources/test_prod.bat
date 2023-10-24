set INSTITUTION=%1
set JAVA_HOME=C:\Labor\jre1.8.0_92\
set M2_HOME=C:\Labor\apache-maven-3.1.1
set PATH=$PATH;C:\Labor\;C:\Labor\jre1.8.0_92\bin;C:\Labor\apache-maven-3.1.1\bin
SET CLASSPATH=.;C:\Labor\sapjco3.jar
java -d64 -version
cd cpx-sap-importer
mvn -o exec:java -Dexec.mainClass="de.lb.cpx.sap.importer.SapImportProcessTest" -Dexec.args="0009 261101527 C:\\Labor\\" -Dexec.cleanupDaemonThreads=false
cd ..
PAUSE
REM set /p DUMMY=Hit ENTER to continue...