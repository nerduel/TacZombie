# TacZombie - A tactical Pacman clone

This game was deceloped by three students of the university of applied science Konstanz ([HTWG Konstanz](www.htwg-konstanz.de)) for the course "modern programming languages" in the winter semester 2013/2014.

The task was given by our Prof. Dr. Boger. It was to:
- develop a game in scala
- has a graphical ui
- has a textual ui
- has a web ui (Play Framework)
- tests should run fully automated
- use sbt as build tool
- use eclipse as ide
- live the spirit of scala

There are currently two players in this game which have multiple tokens:
- Human
- Zombie

The game is round based.

The aim of the game ist:
- as Human: collect all coins and survive
- as Zombie: chase and kill the Human

The commands are:
- <←>, <↑>, <→>, <↓> to navigate on the map
- \<g> to switch between tokens
- \<f> to respawn a dead token (Human: only if lifes left)
- \<n> to switch to next player
- \<m> to create a new map
- \<r> to reset current map
- Gui & Tui: \<q> to quit

## Presentation

TODO: LINK TO YOUTUBE VIDEO

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

Have fun & enjoy playing!

