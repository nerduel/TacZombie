organization in ThisBuild := "org.htwg"

scalaVersion in ThisBuild := "2.10.2"

name in ThisBuild := "tacZombie"

version := "1.0"

libraryDependencies in ThisBuild ++= Seq("org.specs2" %% "specs2" % "2.3.8" % "test",
					 "io.spray" %%  "spray-json" % "1.2.5")

