build:
	javac anairc/Anairc.java
clean:
	rm anairc/*class
	rm gui/*class
	rm net/*class
	rm swgui/*class
	rm tree/*class
	rm users/*class
	rm util/*class
	rm util/log/*class

jar: build
	jar cfm ../anairc.jar META-INF/MANIFEST.MF *

start: build
	java anairc.Anairc
