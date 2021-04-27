import sbt.Keys._
import sbt._
import uk.gov.hmrc.versioning.SbtGitVersioning

val libName = "play-conditional-form-mapping"

val compileDependencies = PlayCrossCompilation.dependencies(
  play26 = Seq(
    "com.typesafe.play" %% "play" % "2.6.20"
  ),
  play27 = Seq(
    "com.typesafe.play" %% "play" % "2.7.5"
  )
)

val testDependencies = PlayCrossCompilation.dependencies(
  shared = Seq(
    "org.scalatest" %% "scalatest" % "3.0.8" % "test",
    "org.pegdown" % "pegdown" % "1.6.0" % "test"
  )
)

lazy val playConditionalFormMapping = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    name := libName,
    majorVersion := 1,
    makePublicallyAvailableOnBintray := true,
    libraryDependencies ++= compileDependencies ++ testDependencies,
    resolvers := Seq("typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/"),
    crossScalaVersions := List(scala212, scala211),
    scalaVersion := scala212
  )
  .settings(PlayCrossCompilation.playCrossCompilationSettings)

lazy val scala212 = "2.12.10"
lazy val scala211 = "2.11.12"

