//name := "sds-task-scala"
//scalaVersion := "2.13.10"
//
//ThisBuild / scalaVersion := "2.13.10"
////ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % "2.1.0"
//ThisBuild / evictionErrorLevel := Level.Warn
//
//lazy val root = (project in file("."))
////  .enablePlugins(PlayScala)
////  .enablePlugins(JavaAppPackaging)
//  .settings(
//    libraryDependencies ++= Seq(
//      "com.typesafe.play" %% "play" % "2.8.0",
////      "com.typesafe.play" %% "twirl-api" % "1.6.8",
////      "com.typesafe.play" %% "play-json" % "2.10.5",
////      "org.mongodb.scala" %% "mongo-scala-driver" % "5.1.1",
////      "com.nulab-inc" %% "play2-oauth2-provider" % "2.0.0",
////      "com.typesafe" % "config" % "1.4.3",
////      "ch.qos.logback" % "logback-classic" % "1.2.3",
////      "org.scalatest" %% "scalatest" % "3.2.19" % "test",
////      "org.scalamock" %% "scalamock" % "6.0.0" % Test,
////      "com.typesafe.play" %% "play-slick" % "5.3.0",
//    )
//  )

name := "sds-test-scala"

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "5.1.1"
libraryDependencies += "com.typesafe.play" %% "play-ahc-ws" % "2.9.4"
