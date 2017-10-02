#!/bin/sh

java -cp src:build:build/presence.jar \
    -Djava.rmi.server.codebase=http://localhost:8080/build/ \
    -Djava.rmi.server.hostname=localhost \
    -Djava.security.policy=client.policy \
    client.ChatClient $1

#java -cp src:build:build/presence.jar \
#    -Djava.rmi.server.codebase=http://localhost:8080/build/ \
#    -Djava.rmi.server.hostname=localhost \
#    -Djava.security.policy=client.policy \
#    client.ChatClient sanchrob
