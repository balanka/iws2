import sbt.Keys._
import sbt.Project.projectToRef

// a special crossProject for configuring a JS/JVM/shared structure
lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(
    scalaVersion := Settings.versions.scala,
    libraryDependencies ++= Settings.sharedDependencies.value
     // "com.github.japgolly.scalajs-react" %%% "ext-monocle" % "0.10.4"
  )
  // set up settings specific to the JS project
  .jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJVM = shared.jvm.settings(name := "sharedJVM")

lazy val sharedJS = shared.js.settings(name := "sharedJS")

// use eliding to drop some debug code in the production build
lazy val elideOptions = settingKey[Seq[String]]("Set limit for elidable functions")

// instantiate the JS project for SBT with some additional settings
lazy val client: Project = (project in file("client"))
  .settings(
    name := "client",
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    libraryDependencies ++= Settings.scalajsDependencies.value,
    // by default we do development build, no eliding
    elideOptions := Seq(),
    scalacOptions ++= elideOptions.value,
    jsDependencies ++= Settings.jsDependencies.value,
    // RuntimeDOM is needed for tests
    jsDependencies += RuntimeDOM % "test",
    // yes, we want to package JS dependencies
    skip in packageJSDependencies := false,
    // use Scala.js provided launcher code to start the client app
    persistLauncher := true,
    persistLauncher in Test := false,
    // use uTest framework for tests
    testFrameworks += new TestFramework("utest.runner.Framework")
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSPlay)
  .dependsOn(sharedJVM)
  .dependsOn(sharedJS)
  .aggregate(macros, clientmacros)
  .dependsOn(macros)
  .dependsOn(clientmacros)
// Client projects (just one in this case)
lazy val clients = Seq(client)





lazy val gen = Project(id = "generate", base = file("gen"))
val myCodeGenerator = TaskKey[Seq[File]]("mycode-generate", "Generate My Awesome Code")

lazy val clientmacros: Project = (project in file("clientmacros"))
  .settings(
    name := "clientmacros",
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    libraryDependencies ++= Settings.scalajsDependencies.value,

    // by default we do development build, no eliding
    elideOptions := Seq(),
    scalacOptions ++= elideOptions.value,
    jsDependencies ++= Settings.jsDependencies.value
    // RuntimeDOM is needed for tests
    //jsDependencies += RuntimeDOM % "test",
    // yes, we want to package JS dependencies
   // skip in packageJSDependencies := false,
    // use Scala.js provided launcher code to start the client app
    //persistLauncher := false,
    //persistLauncher in Test := false,
    // use uTest framework for tests
 //   testFrameworks += new TestFramework("utest.runner.Framework"),

 // sourceGenerators in Compile <+= (myCodeGenerator in Compile),

  /*sourceGenerators in Compile <+= sourceManaged in Compile map { dir =>
  val file = dir / "com" / "kabasoft" / "iws" / "gui" / "macros" / "GeneratedImplicits.scala"

  val f0_22 = (0 to 22).map { arity =>
    val argsParam = (1 to arity).map(i => s"T$i").mkString(",")
    val params = if (argsParam.isEmpty) "R" else s"$argsParam,R"
    s"""
       |implicit def function${arity}Writer[$params]: JsWriter[Function$arity[$params]] = {
       |  new JsWriter[Function$arity[$params]] {
       |    override def toJs(value: Function$arity[$params]): js.Any = fromFunction$arity(value)
       |  }
       |}""".stripMargin
  }.mkString("\n")

  IO.write(file, s"""
                    |package com.kabasoft.iws.gui.macros
                    |
                    |import scala.scalajs.js
                    |import scala.scalajs.js.Any._
                    |
                    |trait GeneratedImplicits {
                    |  $f0_22
                    |}
    """.stripMargin
  )

  Seq(file)
} */
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSPlay)
  //.dependsOn(gen)
  .dependsOn(sharedJVM)
  .dependsOn(sharedJS)
  .dependsOn(macros)


lazy val macros = (project in file("macros"))
  .settings(
      name := "macros",
      version := Settings.version,
      scalaVersion := Settings.versions.scala,
      scalacOptions ++= Settings.scalacOptions,
      libraryDependencies ++= Settings.jvmDependencies.value
  )
  .dependsOn(sharedJVM)

// instantiate the JVM project for SBT with some additional settings
lazy val server = (project in file("server"))
  .settings(
    name := "server",
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    libraryDependencies ++= Settings.jvmDependencies.value,
    commands += ReleaseCmd,
    // connect to the client project
    scalaJSProjects := clients,
    pipelineStages := Seq(scalaJSProd),
    // compress CSS
    LessKeys.compress in Assets := true
  )
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin) // use the standard directory layout instead of Play's custom
  .aggregate(clients.map(projectToRef): _*)
  .dependsOn(sharedJVM)
  .dependsOn(macros)

// Command for building a release
lazy val ReleaseCmd = Command.command("release") {
  state => "set elideOptions in client := Seq(\"-Xelide-below\", \"WARNING\")" ::
    "client/clean" ::
    "client/test" ::
    "server/clean" ::
    "server/test" ::
    "server/dist" ::
    "set elideOptions in client := Seq()" ::
    state
}

// lazy val root = (project in file(".")).aggregate(client, server)

// loads the Play server project at sbt startup

onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value


