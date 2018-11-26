#!/bin/sh

# You can set JAVA_HOME and JAVA_OPTS variables:
# JAVA_HOME points to JVM
# JAVA_OPTS sets JVM options

JAVA_CMD=java
if [ ! -z "$JAVA_HOME" ] ; then
  JAVA_CMD=$JAVA_HOME/bin/java
fi
CONF=conf
LIBS=lib/xsltc.jar:lib/xml-apis.jar:lib/xercesImpl.jar:lib/xalan.jar:lib/velocity-dep-1.4.jar:lib/velocity-1.4.jar:lib/spring-2.5.4.jar:lib/serializer.jar:lib/quartz-all-1.6.1-RC1.jar:lib/quartz-1.6.1-RC1.jar:lib/opeserver.jar:/lib/ojdbc14.jar:lib/myant.jar:lib/log4j-1.2.9.jar:lib/commons-net-20070730.jar:lib/commons-logging-1.1.jar:lib/commons-lang.jar:lib/commons-collections-3.2.jar:lib/commons-codec-1.3.jar:lib/classes12.jar:lib/bcprov-jdk15-140.jar:lib/acegi-security-1.0.7.jar:lib/commons-io-1.4.jar:
echo "$JAVA_CMD" $JAVA_OPTS -cp "$CONF:$LIBS" Launcher
"$JAVA_CMD" $JAVA_OPTS -cp "$CONF:$LIBS" Launcher
