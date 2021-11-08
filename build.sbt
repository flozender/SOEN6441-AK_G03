name := """Pedram"""
organization := "Pedram"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies ++= Seq(
  javaWs
)
libraryDependencies += ehcache