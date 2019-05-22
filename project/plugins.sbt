resolvers ++= Seq(
	Resolver.url("hmrc-sbt-plugin-releases",
  url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/")

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "1.16.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-git-versioning" % "1.19.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-artifactory" % "0.19.0")