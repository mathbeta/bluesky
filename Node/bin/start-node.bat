@title NODE

@call "env.bat" %*

@REM Set debug options
set DEBUG_PORT=8090
set JAVA_DEBUG=-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=%DEBUG_PORT%,server=y,suspend=n

%JAVA_HOME%/bin/java -cp ../configs/node_conf;../lib/startup.jar -Dstorage.classpath=../lib %JAVA_DEBUG% com.dc.storage.main.Start node