import sbt._

object LibDependencies {

  val compileDependencies =
    "org.playframework" %% "play" % "3.0.9"

  val testDependencies = Seq(
      "org.scalatest"          %% "scalatest"    % "3.2.17",
      "com.vladsch.flexmark"   %  "flexmark-all" % "0.64.8"
    ).map(_ % Test)

}
