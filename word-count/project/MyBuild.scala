import sbt._
import Keys._

object BuildSettings {
  val buildOrganization = "vg.inc"
  val buildVersion      = "0.1"
  val buildScalaVersion = "2.10.2"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    shellPrompt  := ShellPrompt.buildShellPrompt
  )
}

object Resolvers {
  val sunrepo    = "Sun Maven2 Repo" at "http://download.java.net/maven/2"
  val sunrepoGF  = "Sun GF Maven2 Repo" at "http://download.java.net/maven/glassfish"
  val oraclerepo = "Oracle Maven2 Repo" at "http://download.oracle.com/maven"

  val oracleResolvers = Seq (sunrepo, sunrepoGF, oraclerepo)
}

// Shell prompt which show the current project,
// git branch and build version
object ShellPrompt {
  object devnull extends ProcessLogger {
    def info (s: => String) {}
    def error (s: => String) { }
    def buffer[T] (f: => T): T = f
  }
  def currBranch = (
    ("git status -sb" lines_! devnull headOption)
      getOrElse "-" stripPrefix "## "
    )

  val buildShellPrompt = {
    (state: State) => {
      val currProject = Project.extract (state).currentProject.id
      "%s:%s:%s> ".format (
        currProject, currBranch, BuildSettings.buildVersion
      )
    }
  }
}

object MyBuild extends Build {
  import Resolvers._
  import BuildSettings._

  val logbackVer = "1.0.13"

  val commonDeps = Seq(
    "ch.qos.logback" % "logback-core" % logbackVer,
    "ch.qos.logback" % "logback-classic" % logbackVer,
    "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
    "junit" % "junit" % "4.8" % "test"
  )

  lazy val myBuild = Project("MyBuild", file("."),
    settings = buildSettings ++ Seq(libraryDependencies ++= commonDeps))

}