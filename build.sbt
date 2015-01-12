name := "w"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.4"

resolvers += DefaultMavenRepository

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-library" % "2.11.4",
  "org.apache.pivot" % "pivot-core" % "2.0.4",
  "org.apache.pivot" % "pivot-wtk" % "2.0.4",
  "org.apache.pivot" % "pivot-wtk-terra" % "2.0.4",
  "commons-codec" % "commons-codec" % "1.10",
  "commons-io" % "commons-io" % "2.4",
  "io.reactivex" % "rxscala_2.11" % "0.23.0",
  "org.mapdb" % "mapdb" % "1.0.6",
  "org.slf4j" % "slf4j-api" % "1.7.6",
  "ch.qos.logback" % "logback-core" % "1.1.2",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "junit" % "junit" % "4.11" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.2.2" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test"
)

libraryDependencies += "net.coobird" % "thumbnailator" % "0.4.8"


scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

transitiveClassifiers := Seq("sources")