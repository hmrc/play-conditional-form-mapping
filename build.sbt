import sbt.Keys._
import sbt._
import uk.gov.hmrc.versioning.SbtGitVersioning

val appName = "play-conditional-form-mapping"

lazy val appDependencies = Seq(
  "com.typesafe.play" %% "play" % "2.5.19",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.pegdown" % "pegdown" % "1.6.0" % "test"
)

lazy val playConditionalFormMapping = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    name := appName,
    majorVersion := 1,
    scalaVersion := "2.11.12",
    libraryDependencies ++= appDependencies,
    resolvers := Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      "typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/"
    ),
    crossScalaVersions := Seq("2.11.12")
  )