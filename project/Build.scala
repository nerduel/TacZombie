import sbt._
import Keys._

object TacZombieBuild extends Build {
    lazy val root = Project(id = "taczombie", 
                            base = file(".")) aggregate(model,wui)

    lazy val model = Project(id = "taczombie-model", base = file("model"))

    lazy val wui = Project(id = "taczombie-wui", base = file("wui")) dependsOn(model)
}
