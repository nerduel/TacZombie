libraryDependencies ++= Seq("org.scala-lang" % "scala-swing" % "2.10.0",
    			     "net.codingwell" % "scala-guice_2.10" % "4.0.0-beta",
			     "net.codingwell" %% "scala-guice" % "4.0.0-beta")

unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))



