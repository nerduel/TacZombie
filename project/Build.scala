import sbtassembly.Plugin._
import AssemblyKeys._
import sbt._
import Keys._

object TacZombieBuild extends Build {
lazy val root = Project(
	id = "taczombie", 
	base = file("."), 
	aggregate = Seq(model,wui,gui)
	) settings (ScctPlugin.mergeReportSettings: _*)
    
lazy val model = Project(
	id = "model", 
	base = file("model")) settings (ScctPlugin.instrumentSettings: _*)

lazy val wui = Project(id = "wui",
	base = file("wui")) settings (ScctPlugin.instrumentSettings: _*) dependsOn(model)

lazy val gui = Project(id = "gui",
	base = file("gui"),
	dependencies = Seq(model)).settings(assemblySettings: _*).
	settings (ScctPlugin.instrumentSettings: _*).
	settings(jarName in assembly := "TacZombieClient.jar").
	settings(test in assembly := {}).
	settings(mainClass in assembly := Some("taczombie.client.view.main.Main")).
	settings(mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
		{
    			case PathList(ps @ _*) if ps.contains("META-INF") => MergeStrategy.discard
			case x => old(x)
  		}
	})
}
