@title CONSOLE

@echo off

@call "env.bat" %*

@REM Set debug options
set DEBUG_PORT=8091
set JAVA_DEBUG=-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=%DEBUG_PORT%,server=y,suspend=n

%JAVA_HOME%/bin/java -cp ../configs/console_conf;../lib/startup.jar -Dfile.encoding=utf-8 -Dstorage.classpath=../lib %JAVA_DEBUG% -Djava.util.logging.config.file=../configs/console_conf/logging.properties com.mathbeta.storage.main.Start console