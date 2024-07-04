import LibDependencies.PlayVersion

val libName = "play-conditional-form-mapping"

val scala2_13 = "2.13.12"
val scala3 = "3.3.3"

ThisBuild / scalaVersion       := scala2_13
ThisBuild / majorVersion       := 3
ThisBuild / isPublicArtefact   := true

lazy val playConditionalFormMapping = (project in file("."))
  .settings(publish / skip := true)
  .aggregate(
    play29,
    play30
  )

lazy val play29 = Project(s"$libName-play-29", file("play-29"))
  .settings(
    crossScalaVersions := Seq(scala2_13),
    libraryDependencies ++= LibDependencies.compileDependencies(PlayVersion.Play29) +: LibDependencies.testDependencies,
    sharedSources
  )

lazy val play30 = Project(s"$libName-play-30", file("play-30"))
  .settings(
    crossScalaVersions := Seq(scala3, scala2_13),
    libraryDependencies ++= LibDependencies.compileDependencies(PlayVersion.Play30) +: LibDependencies.testDependencies,
    sharedSources
  )

def sharedSources = Seq(
  Compile / unmanagedSourceDirectories += baseDirectory.value / "../shared/src/main/scala",
  Compile / unmanagedResourceDirectories += baseDirectory.value / "../shared/src/main/resources",
  Test / unmanagedSourceDirectories += baseDirectory.value / "../shared/src/test/scala",
  Test / unmanagedSourceDirectories += (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, _)) =>
      baseDirectory.value / "../shared/src/test/scala-2"
    case _ =>
      baseDirectory.value / "../shared/src/test/scala-3"
  }),
  Test / unmanagedResourceDirectories += baseDirectory.value / "../shared/src/test/resources"
)