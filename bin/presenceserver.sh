#!/bin/sh

java -cp src:build:build/presence.jar \
    -Djava.rmi.server.codebase=http://localhost:8080/build/presence.jar \
    -Djava.rmi.server.hostname=localhost \
    -Djava.security.policy=server.policy server.PresenceServer &
