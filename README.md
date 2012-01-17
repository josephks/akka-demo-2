Demo Akka project, based on Akka 2.

To run:

```
cd remote1
./sbt
```

At the sbt prompt:

```
compile
genrate-scripts
```

In terminal window 1:
```bash remote1/target/scala-2.9.1/classes/start-server.sh```

In terminal window 2:
```bash remote1/target/scala-2.9.1/classes/ping.sh```

Instead of the "pong" expected, output on client side is:
```
host: null port: 2222
got fut
rec from server: Left(akka.actor.ActorKilledException: In DeadLetterActorRef - promises are always broken.
[192.168.1.3_aba32320-412b-11e1-b16f-4ab08a6deec0])
```
