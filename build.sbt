lazy val commonSettings = Seq(
  organization       := "ch.megard",
  version            := "1.1.4-SNAPSHOT",
  scalaVersion       := "2.13.8",
  crossScalaVersions := Seq(scalaVersion.value, "2.12.15", "3.1.1"),
  resolvers += "Apache Nexus Snapshots".at("https://repository.apache.org/content/repositories/snapshots/"),
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-unchecked",
    "-deprecation"
  ),
  javacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-source",
    "8",
    "-target",
    "8"
  ),
  homepage := Some(url("https://github.com/lomigmegard/akka-http-cors")),
  licenses := Seq("Apache 2" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/lomigmegard/akka-http-cors"),
      "scm:git@github.com:lomigmegard/akka-http-cors.git"
    )
  ),
  developers := List(
    Developer(id = "lomigmegard", name = "Lomig Mégard", email = "", url = url("https://lomig.ch"))
  )
)

lazy val publishSettings = Seq(
  publishMavenStyle      := true,
  Test / publishArtifact := false,
  pomIncludeRepository   := { _ => false },
  publishTo              := sonatypePublishToBundle.value
)

lazy val dontPublishSettings = Seq(
  publish / skip := true
)

lazy val root = (project in file("."))
  .aggregate(`akka-http-cors`, `akka-http-cors-example`, `akka-http-cors-bench-jmh`)
  .settings(commonSettings)
  .settings(dontPublishSettings)

lazy val pekkoVersion     = "0.0.0+26592-864ee821-SNAPSHOT"
lazy val pekkoHttpVersion = "0.0.0+4284-374ff95e-SNAPSHOT"

lazy val `akka-http-cors` = project
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(
    // Java 9 Automatic-Module-Name (http://openjdk.java.net/projects/jigsaw/spec/issues/#AutomaticModuleNames)
    Compile / packageBin / packageOptions += Package.ManifestAttributes(
      "Automatic-Module-Name" -> "ch.megard.akka.http.cors"
    ),
    libraryDependencies += "org.apache.pekko" %% "pekko-http"   % pekkoHttpVersion cross CrossVersion.for3Use2_13,
    libraryDependencies += "org.apache.pekko" %% "pekko-stream" % pekkoVersion % Provided cross CrossVersion.for3Use2_13,
    libraryDependencies += "org.apache.pekko" %% "pekko-http-testkit" % pekkoHttpVersion % Test cross CrossVersion.for3Use2_13,
    libraryDependencies += "org.apache.pekko" %% "pekko-stream-testkit" % pekkoVersion % Test cross CrossVersion.for3Use2_13,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % Test
  )

lazy val `akka-http-cors-example` = project
  .dependsOn(`akka-http-cors`)
  .settings(commonSettings)
  .settings(dontPublishSettings)
  .settings(
    libraryDependencies += "org.apache.pekko" %% "pekko-stream" % pekkoVersion cross CrossVersion.for3Use2_13
    // libraryDependencies += "ch.megard" %% "akka-http-cors" % version.value
  )

lazy val `akka-http-cors-bench-jmh` = project
  .dependsOn(`akka-http-cors`)
  .enablePlugins(JmhPlugin)
  .settings(commonSettings)
  .settings(dontPublishSettings)
  .settings(
    libraryDependencies += "org.apache.pekko" %% "pekko-stream" % pekkoVersion cross CrossVersion.for3Use2_13
  )
