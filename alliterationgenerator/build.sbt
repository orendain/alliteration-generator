name := """alliterationgenerator"""

version := "1.0.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

// Need to wait for play-slick 0.8 for scala 2.11 support
//scalaVersion := "2.11.1"
scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  filters,
  "org.webjars" % "bootstrap-sass" % "3.1.1",
  "org.webjars" % "jquery" % "1.11.1",
  "org.webjars" % "requirejs" % "2.1.11-1",
  //"com.typesafe.slick" % "slick_2.11.0-RC4" % "2.1.0-M",
  //"com.typesafe.slick" %% "slick" % "2.1.0-M2",
  "mysql" % "mysql-connector-java" % "5.1.30",
  "org.postgresql" % "postgresql" % "9.3-1101-jdbc41",
  "com.typesafe.play" %% "play-slick" % "0.7.0-M1",
  "net.debasishg" %% "redisclient" % "2.13",
  "org.apache.commons" % "commons-email" % "1.3.2",
  //"org.scaldi" %% "scaldi" % "0.3.2",
  "org.scaldi" %% "scaldi-play" % "0.3.3"
  //"org.specs2" %% "specs2" % "2.3.12"
)
