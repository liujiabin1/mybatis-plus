#!/bin/sh

def_port=8000

PORT=`[ -z "$1" ] && echo "$def_port" || echo "$1"`

if [ "$PORT" -eq "$def_port" ]
then
    mvn clean -Pdev spring-boot:run -Dspring-boot.run.arguments="--server.port=$PORT" -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,address=1310,suspend=n,server=y"
else
    mvn clean -Pdev spring-boot:run -Dspring-boot.run.arguments="--server.port=$PORT"
fi
