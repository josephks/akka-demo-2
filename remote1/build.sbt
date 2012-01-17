/** Project */
name := "Akka Demo"

version := "0.1"

organization := "net.tupari"

scalaVersion := "2.9.1"


seq(  net.tupari.`sbt-script-generation-plugin`.ScriptGenPlugin.scriptGenSettings : _*)

            scriptsToGenerate := Seq(
      ("start-server.sh" ->  "demo.AkkaBcastMain " ) -> List("2222") ,
      ("run-setter.sh" ->  "demo.AkkaBcastTestClient") ->  List("2222",  "$@"),
      ("start-sink.sh" -> "demo.AkkaBcastTestSink") -> List("2222") ,
       ("ping.sh" -> "demo.Pinger")  ->  List("2222",  "$@")
        )

/** Shell */
shellPrompt := { state => System.getProperty("user.name") + " " +System.getProperty("user.dir") + "> " }

shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }




/** Compilation */
javacOptions ++= Seq("-Xmx1812m", "-Xms512m", "-Xss4m")

scalacOptions += "-deprecation"

maxErrors := 20

pollInterval := 1000

testFrameworks += new TestFramework("org.specs2.runner.SpecsFramework")

testOptions := Seq(Tests.Filter(s =>
  Seq("Spec", "Suite", "Unit", "Specs", "Test", "all").exists(s.endsWith(_)) &&
    ! s.endsWith("FeaturesSpec") ||
    s.contains("UserGuide") || 
    s.matches("org.specs2.guide.*")))

/** Console */
initialCommands in console := "import org.specs2._"
