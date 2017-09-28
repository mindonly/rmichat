java -cp src/:build/:build/compute.jar \
    -Djava.rmi.server.codebase=http://localhost/compute.jar \
    -Djava.security.policy=client.policy \
    client.ComputePi localhost 32
