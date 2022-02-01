import sbt.Keys._
import sbt._
import uk.gov.hmrc.versioning.SbtGitVersioning

val libName = "play-conditional-form-mapping"

val compileDependencies = PlayCrossCompilation.dependencies(
  play28 = Seq(
    "com.typesafe.play" %% "play" % "2.8.7"
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
    scalaVersion := scala212
  )
  .settings(PlayCrossCompilation.playCrossCompilationSettings)

lazy val scala212 = "2.12.10"
