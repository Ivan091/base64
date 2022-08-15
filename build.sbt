ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.13" % "test"
libraryDependencies += "org.scalatestplus" %% "scalacheck-1-16" % "3.2.12.0" % "test"

lazy val root = (project in file("."))
  .settings(
    name := "base64",
    idePackagePrefix := Some("com.meineliebe")
  )
