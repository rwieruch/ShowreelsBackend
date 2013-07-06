import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Showreels"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "net.sf.flexjson" % "flexjson" % "2.1",
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
