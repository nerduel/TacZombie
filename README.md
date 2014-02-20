# TacZombie - A tactical Pacman clone

This game was deceloped by three students of the University of applied science Konstanz ([HTWG-Konstanz](www.htwg-konstanz.de)) for the course "Modern programming languages" in the winter semester 2013/2014.

The task was to:
- develop a game in scala
- has a graphical ui
- has a textual ui
- has a web ui (Play Framework)
- tests should run fully automated
- use sbt as build tool
- use eclipse as ide
- live the spirit of scala

## Play

### Run WebServer (incl. Wui)

``` bash
cd taczombie/
sbt "project wui" run
```

### Run Tui

``` bash
cd taczombie/
sbt
> project gui
> run tui
```

### Run Gui
``` bash
cd taczombie/
sbt
> project gui
> run
```

## Test

### Run all tests
``` bash
cd taczombie/
$ sbt clean scct:test
```

### Merge coverage data for nice html view
``` bash
cd taczombie/
$ sbt scct-merge-report
```

## Standalone Version

### Server & Wui

To create a zip file, which contains the web server and therfor the web interface, following steps are necessary:

``` bash
cd taczombie/
sbt "project wui" dist
```

After extracting the zip to your favourite location you can execute it:

``` bash
cd webServerStandAlone/
chmod +x bin/wui
./bin/wui
```

### Gui & Tui

To create an executable *.jar file following steps are neccessary:

``` bash
cd taczombie/
sbt assembly
```

To execute the gui from the resulting TacZombieClient.jar:

``` bash
java -jar TacZombieClient.jar
```

``` bash
java -jar TacZombieClient.jar tui
```

