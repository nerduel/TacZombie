organization in ThisBuild := "org.htwg"

scalaVersion in ThisBuild := "2.10.2"

name in ThisBuild := "tacZombie"

version := "0.2"

libraryDependencies in ThisBuild ++= Seq("org.specs2" %% "specs2" % "2.3.4" % "test",
					 "io.spray" %%  "spray-json" % "1.2.5")

