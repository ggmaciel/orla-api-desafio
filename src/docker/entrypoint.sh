#!/bin/sh
java -Dserver.port=8080 -Dspring.datasource.url=jdbc:postgresql://db:5432/orla_api?ApplicationName=orla_api -XX:MaxRAMPercentage=70 -jar /opt/orla-api.jar