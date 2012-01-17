import sbt._

import sbt._
import Keys._

object MyBuild extends Build{

  lazy val userHome = System.getProperties.getProperty("user.home")

  private val akkaVer = "2.0-M2"

  lazy val root = Project("root", file(".")) settings(
    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.7" % "test",
      //      "org.specs2" %% "specs2" % "1.6.1",
      //      "org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test",
      "com.typesafe.akka" % "akka-actor" % akkaVer,
      "com.typesafe.akka" % "akka-remote" % akkaVer 
      //          "com.google.protobuf" % "protobuf-java"   % "2.4.1"

      //     "info.cukes" % "cucumber-core" % "1.0.0-SNAPSHOT"  % "test" ,
      //                                                      "org.jruby" % "jruby" % "1.6.4"    % "test"


      //  "org.scala-tools.testing" %% "scalacheck" % "1.9", 
      //  "org.scala-tools.testing" % "test-interface" % "0.5", 
      //  "org.hamcrest" % "hamcrest-all" % "1.1",
      //  "org.mockito" % "mockito-all" % "1.8.5",
    )  ,
    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

  ) //settings
}
