# ing-sw-2020-merli-naro-occhinegro
Software Engineering project 2020

### Group components

10578363 - [Merli Davide Luca](https://github.com/davidemerli)

10610374 - [Naro Gianmarco](https://github.com/gianmarconaro)

10605439 - [Occhinegro Pasquale](https://github.com/PasqualeOcchinegro)


### Development
The project as been developed using [Java 13](https://www.oracle.com/java/technologies/javase-jdk13-downloads.html)

GUI has been written using [JavaFX](https://openjfx.io/)

### How to create a jar
open cmd/shell and run the following maven goal:

```shell script
mvn clean test package shade:shade
```

Maven will run the tests on the generated classes, and they do take a bit of time
(undo functionality makes every end of a turn waste 5 seconds)

Maven Shade has been configured to pack into the jar file all JavaFX dependencies
for Linux/MacOS/Windows graphics engines.

Although there wasn't the possibility to check whether the jar works on MacOS, 
the binaries for JavaFX are there and since it works on Linux there should not be any problems.

Maven shade will create **2** jars inside the ```target``` folder:

```original-santorini-1.0-SNAPSHOT.jar```

and
 
 ```santorini-1.0-SNAPSHOT.jar```
 
The second one is the one packed with all javafx dependencies.

An already compiled jar is available at ```/deliveries/final/jar```

### How to run jar

The jar is unique and can start the **CLI**, the **GUI** and the **SERVER** with the correct arguments.

The default run configuration for the jar is set on the GUI.

Double clicking the jar from a Window Manager will launch directly the GUI.

Executing the jar on the command line from bash or cmd with ```--help``` parameter displays the following:

```
Usage: java -jar santorini.jar [OPTION]...
Launching without options will load the Graphical User Interface (GUI)

-S, --server        starts a server on localhost, if no port is specified it will be 34567
-P, --port          specifies the server port (checked only if --server option is called)
-C, --cli           starts a new client with Command Line Interface (CLI)
```

So, as stated above, to launch the **SERVER**:

```
java -jar santorini.jar --server --port PORT_NUMBER
```

omitting the ```--port``` argument will set the server port to **34567**

### CLI

For the best experience with the command line interface it's recommended the use of a terminal with 
support to UTF-8 unicode characters.



To launch the CLI:
```
java -jar santorini.jar --cli
```

### GUI

To launch the GUI:

```
java -jar santorini.jar
```


#### Linux
We had problems launching the JavaFX jar on Linux without a dedicated graphics environment;
to be able to execute the jar correctly there is the need to add 

``` -Dprism.forceGPU=true ```

before the ```-jar``` argument

So the final command to execute the Graphical User Interface will be:

```shell script
java -Dprims.forceGPU=true -jar santorini.jar
```


### Implemented functionalities

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI (JavaFX) | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Advanced Gods (Chronus, Hestia, Poseidon, Triton, Zeus) | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#)|
| Undo | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#)|
| Multiple games | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#)|
| Persistence | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |