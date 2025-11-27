val libName = "play-conditional-form-mapping"

val scala2_13 = "2.13.16"
val scala3 = "3.3.3"

ThisBuild / scalaVersion       := scala2_13
ThisBuild / majorVersion       := 3
ThisBuild / isPublicArtefact   := true

lazy val playConditionalFormMapping = Project(s"$libName-play-30", file("play-30"))
  .settings(
    crossScalaVersions := Seq(scala3, scala2_13),
    libraryDependencies ++= LibDependencies.compileDependencies +: LibDependencies.testDependencies,
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
