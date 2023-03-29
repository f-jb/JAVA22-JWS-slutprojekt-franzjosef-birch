#!/bin/sh
#
#
CATALINA_HOME=~/Projects/tomee/tomee/
$CATALINA_HOME/bin/catalina.sh stop
rm -r $CATALINA_HOME/webapps/enigma*
mvn package
cp target/enigma.war $CATALINA_HOME/webapps
$CATALINA_HOME/bin/catalina.sh start
