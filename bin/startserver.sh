#!/bin/sh

java -cp src:build:build/compute.jar \
    -Djava.rmi.server.codebase=http://localhost:8080/build/compute.jar \
    -Djava.rmi.server.hostname=localhost \
    -Djava.security.policy=server.policy engine.ComputeEngine &
