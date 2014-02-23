name := "reactive_mongodb"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.0",
  "com.typesafe" %% "play-plugins-mailer" % "2.2.0"
)     

play.Project.playScalaSettings
