@echo on
xjc -d src/main/java -p de.lb.cpx.gdv.types -encoding utf-8 src/main/resources/xsd2013/gdv_typen2013.xsd
xjc -d src/main/java -p de.lb.cpx.gdv.messages -encoding utf-8 src/main/resources/xsd2013/gdv2013.xsd

pause