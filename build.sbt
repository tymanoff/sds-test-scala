name := "sds-test-scala"

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "5.1.1"
libraryDependencies += "com.typesafe.play" %% "play-ahc-ws" % "2.9.4"
libraryDependencies += "com.pauldijou" %% "jwt-core" % "5.0.0"
libraryDependencies += "com.pauldijou" %% "jwt-play-json" % "5.0.0"


