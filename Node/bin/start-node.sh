DEPLOY_PATH="../"
export DEPLOY_PATH
 
#!/bin/bash

DEPLOY_PATH=".."
export DEPLOY_PATH

. ${DEPLOY_PATH}/bin/env.sh $*
 
JAVA_OPTIONS="-Xmx512m -Xms256m"
export JAVA_OPTIONS
 
CLASSPATH=${CLASSPATH}:${DEPLOY_PATH}/configs/node_conf:${DEPLOY_PATH}/lib/startup.jar
export CLASSPATH
 
DEBUG_PORT=8090
export DEBUG_PORT
 
JAVA_DEBUG="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=${DEBUG_PORT},server=y,suspend=n"
export JAVA_DEBUG
 
${JAVA_HOME}/bin/java ${JAVA_OPTIONS} ${JAVA_DEBUG} -Dstorage.classpath=../lib com.dc.storage.main.Start node
