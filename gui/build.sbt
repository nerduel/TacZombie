libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10.0"

unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))



