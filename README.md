anairc
======

anairc: not an IRC chat


Anairc uses broadcast UDP packets to comunicate on the local network so all the
clients can talk to each other without a central server, just start it and
talk!

If you worry about the "broadcast" thing, then don't worry, Windows machines
broadcast a lot of crap all the time, it won't make a difference.

Unfortunately when this was written I didn't know python so it's written in
Java.


Compile
=======

Just run

```
$ make
$ make start
```

To compile and execute.

If you happen to not have make installed shame on you! Open the Makefile and
figure it out by yourself.
