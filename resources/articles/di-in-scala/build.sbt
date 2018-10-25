name := "Hello"
organization := "com.example"
scalaVersion := "2.12.3"
version := "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.3",
  "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided",
  "com.softwaremill.macwire" %% "util" % "2.3.0",
  "com.softwaremill.macwire" %% "proxy" % "2.3.0",
  "org.scalamock" %% "scalamock" % "4.0.0" % Test

)
