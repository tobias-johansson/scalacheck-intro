val circeVersion = "0.5.1"

lazy val intro = project
  .in(file("."))
  .settings(
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "org.scalatest"  %% "scalatest"  % "3.0.0"  % "test",
      "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
    )
  )
