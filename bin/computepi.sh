#!/bin/sh

java -cp src:build:build/compute.jar \
    -Djava.rmi.server.codebase=http://localhost:8080/build/ \
    -Djava.rmi.server.hostname=localhost \
    -Djava.security.policy=client.policy \
    client.ComputePi localhost 64 
