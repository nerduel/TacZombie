import sbt._
import Keys._

object TacZombieBuild extends Build {
    lazy val root = Project(id = "taczombie", base = file(".")) settings (ScctPlugin.mergeReportSettings: _*) aggregate(model,wui)

    lazy val model = Project(id = "taczombie-model", base = file("model")) settings (ScctPlugin.instrumentSettings: _*)

    lazy val wui = Project(id = "taczombie-wui", base = file("wui")) settings (ScctPlugin.instrumentSettings: _*) dependsOn(model)
}
