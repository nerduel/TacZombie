libraryDependencies += "org.scalafx" %% "scalafx" % "1.0.0-M7"

unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))



