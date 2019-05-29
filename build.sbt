import sbt.Keys._
import sbt._
import uk.gov.hmrc.versioning.SbtGitVersioning

val appName = "play-conditional-form-mapping"

val compileDependencies = PlayCrossCompilation.dependencies(
  play25 = Seq(
    "com.typesafe.play" %% "play" % "2.5.19"
  ),
  play26 = Seq(
    "com.typesafe.play" %% "play" % "2.6.20"
  )
)

val testDependencies = PlayCrossCompilation.dependencies(
  shared = Seq(
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",
    "org.pegdown" % "pegdown" % "1.6.0" % "test"
  )
)

lazy val playConditionalFormMapping = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    name := appName,
    majorVersion := 1,
    makePublicallyAvailableOnBintray := true,
    libraryDependencies ++= compileDependencies ++ testDependencies,
    resolvers := Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      "typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/"
    ),
    scalaVersion := "2.11.12",
    crossScalaVersions := Seq("2.11.12", "2.12.8")
  )
  .settings(PlayCrossCompilation.playCrossCompilationSettings)