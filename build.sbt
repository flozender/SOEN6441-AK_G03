name := """Gitterific"""
organization := "AK_G03"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies ++= Seq(
  javaWs
)
libraryDependencies += ehcache
val AkkaVersion = "2.6.10"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.6.14" % Test
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "org.asynchttpclient" % "async-http-client" % "2.12.3"
libraryDependencies += "org.awaitility" % "awaitility" % "4.1.1" % Test


