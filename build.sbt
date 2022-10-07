import uk.gov.hmrc.SbtArtifactory.autoImport.makePublicallyAvailableOnBintray

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
    resolvers := Seq("typesafe-releases" at "https://repo.typesafe.com/typesafe/releases/"),
    scalaVersion := scala2_12,
    crossScalaVersions := Seq(scala2_12, scala2_13)
  )
  .settings(PlayCrossCompilation.playCrossCompilationSettings)

val scala2_12 = "2.12.15"
val scala2_13 = "2.13.8"
