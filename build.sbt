name := "reactive_mongodb"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)

libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.11.0-SNAPSHOT"

libraryDependencies += "org.webjars" % "jquery" % "2.1.1"

libraryDependencies += "org.webjars" % "bootstrap" % "3.2.0"

libraryDependencies += "org.webjars" % "font-awesome" % "4.2.0"

libraryDependencies += "org.webjars" % "respond" % "1.4.2"
