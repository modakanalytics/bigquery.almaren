ThisBuild / name := "bigquery.almaren"
ThisBuild / organization := "com.github.music-of-the-ainur"

lazy val scala212 = "2.12.10"
lazy val scala213 = "2.13.9"

crossScalaVersions := Seq(scala212,scala213)
ThisBuild / scalaVersion := scala213

val sparkVersion = "3.3.3"
val majorVersionReg = "([0-9]+\\.[0-9]+).{0,}".r

val majorVersionReg(majorVersion) = sparkVersion

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "com.github.music-of-the-ainur" %% "almaren-framework" % s"0.9.10-${majorVersion}" % "provided",
  "com.google.cloud.spark" %% "spark-bigquery-with-dependencies" % "0.34.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "org.scalatest" %% "scalatest" % "3.2.14" % "test"
)

enablePlugins(GitVersioning)

resolvers += "spring" at "https://repo.spring.io/plugins-release"

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/service/local/repositories/releases/content"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/modakanalytics/bigquery.almaren"),
    "scm:git@github.com:modakanalytics/bigquery.almaren.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "mantovani",
    name  = "Daniel Mantovani",
    email = "daniel.mantovani@modak.com",
    url   = url("https://github.com/music-of-the-ainur")
  ),
  Developer(
    id = "badrinathpatchikolla",
    name = "Badrinath Patchikolla",
    email = "badrinath.patchikolla@modakanalytics.com",
    url = url("https://github.com/music-of-the-ainur")
  )
)

ThisBuild / description := "BigQuery Connector For Almaren Framework"
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/modakanalytics/bigquery.almaren"))
ThisBuild / organizationName := "Modak Analytics"
ThisBuild / organizationHomepage := Some(url("https://github.com/modakanalytics"))

// Remove all additional repository other than Maven Central from POM
credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credential")

ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

ThisBuild / publishMavenStyle := true
updateOptions := updateOptions.value.withGigahorse(false)

