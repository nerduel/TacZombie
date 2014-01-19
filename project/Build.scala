import sbt._
import Keys._

object TacZombieBuild extends Build {
    lazy val root = Project(id = "taczombie", base = file(".")) 
        settings (ScctPlugin.mergeReport: _*) aggregate(model)

    lazy val model = Project(id = "taczombie-model", base = file("model"))
        settings (ScctPlugin.instrumentSettings: _*)
}
