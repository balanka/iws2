import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

/**
 * Application settings. Configure the build for your application here.
 * You normally don't have to touch the actual build definition after this.
 */
object Settings {
  /** The name of your application */
  val name = "IWS"

  /** The version of your application */
  val version = "1.1.3"

  /** Options for the scala compiler */
  val scalacOptions = Seq(
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-feature"
    //"-Xfatal-warnings"

  )

  /** Declare global dependency versions here to avoid mismatches in multi part dependencies */
  object versions {
    val scala = "2.11.8"
    val scalaDom = "0.9.0"
    //val scalajsReact = "0.10.4"
    val scalajsReact = "0.11.1"
    val monocleVersion = "1.2.1"
    val scalaz ="7.1.2"
    val scalazstream="0.7.2a"
    val scalaCSS = "0.4.1"
    val log4js = "1.4.10"
    val autowire = "0.2.5"
    val booPickle = "1.1.2"
    //val diode = "0.5.0"
    val diode ="1.0.0"
    val doobie ="0.2.3"
    val uTest = "0.3.1"
    //val react = "0.14.7"
    val react = "15.0.1"
    val jQuery = "1.12.0"
    val bootstrap = "3.3.6"
    val chartjs = "1.0.1"
    val playScripts = "0.4.0"
    val javaTime ="0.1.0"
    val jsjoda = "1.0.2"
    val js_joda ="1.1.8"
    val akka ="2.4.4"
    val meta="1.2.0"
  }

  /**
   * These dependencies are shared between JS and JVM projects
   * the special %%% function selects the correct version for each project
   */
  val sharedDependencies = Def.setting(Seq(
    "com.lihaoyi" %%% "autowire" % versions.autowire,
    "me.chrons" %%% "boopickle" % versions.booPickle,
    "org.scalaz"  %% "scalaz-core" % versions.scalaz,
    "org.scalaz" %% "scalaz-effect" % versions.scalaz,
    "org.scalaz.stream" %% "scalaz-stream" % versions.scalazstream,
    "org.tpolecat"%% "doobie-core" % versions.doobie,
    "org.tpolecat"%% "doobie-contrib-postgresql" % versions.doobie,
    //"com.github.julien-truffaut"  %%%  "monocle-macro"  % versions.monocleVersion,
    //"com.github.japgolly.scalajs-react" %%% "ext-monocle" % "0.11.0",
    //"com.github.nscala-time" %% "nscala-time" % "2.12.0",
    //"org.scalameta" %%%"scalameta" % versions.meta,
    "com.lihaoyi" %%% "utest" % versions.uTest
  ))

  /** Dependencies only used by the JVM project */
  val jvmDependencies = Def.setting(Seq(
    "com.vmunier" %% "play-scalajs-scripts" % versions.playScripts,
    "org.webjars" % "font-awesome" % "4.3.0-1" % Provided,
    "org.webjars" % "bootstrap" % versions.bootstrap % Provided,
    "com.typesafe.akka" %% "akka-actor" %  versions.akka,
    "com.typesafe.akka" %% "akka-remote" %   versions.akka,
    "com.typesafe.akka" % "akka-slf4j_2.11" % versions.akka
  ))

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(Seq(
    "com.github.japgolly.scalajs-react" %%% "core" % versions.scalajsReact,
    "com.github.japgolly.scalajs-react" %%% "extra" % versions.scalajsReact,
    "com.github.japgolly.scalacss" %%% "ext-react" % versions.scalaCSS,
    "me.chrons" %%% "diode" % versions.diode,
    "me.chrons" %%% "diode-react" % versions.diode,
    "org.scala-js" %%% "scalajs-dom" % versions.scalaDom,
    "com.github.chandu0101.scalajs-react-components" %%% "core" % "0.4.1",
    "com.github.chandu0101.scalajs-react-components" %%% "macros" % "0.4.1",
    "com.zoepepper" %%% "scalajs-jsjoda" % versions.jsjoda,
   // "org.scala-js" %%% "scalajs-java-time" % versions.javaTime,
    // "com.github.japgolly.scalajs-react" %%% "ext-scalaz72" %  versions.scalajsReact
    "io.github.widok" %%% "scala-js-momentjs" % "0.1.5"
    //"com.github.japgolly.scalajs-react" %%% "ext-monocle" % versions.monocleVersion
    //"com.acework" %%% "core" % "0.0.1-SNAPSHOT",
    //"com.acework" %%% "macro" % "0.0.1-SNAPSHOT"
  ))

  /** Dependencies for external JS libs that are bundled into a single .js file according to dependency order */
  val jsDependencies = Def.setting(Seq(
    "org.webjars.bower" % "react" % versions.react / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
    "org.webjars.bower" % "react" % versions.react / "react-dom.js" minified "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM",
    "org.webjars" % "jquery" % versions.jQuery / "jquery.js" minified "jquery.min.js",
    "org.webjars" % "bootstrap" % versions.bootstrap / "bootstrap.js" minified "bootstrap.min.js" dependsOn "jquery.js",
    "org.webjars" % "chartjs" % versions.chartjs / "Chart.js" minified "Chart.min.js",
   // "org.webjars.npm" % "js-joda" % versions.js_joda/ "dist/js-joda.js" minified "dist/js-joda.min.js",
    "org.webjars" % "log4javascript" % versions.log4js / "js/log4javascript_uncompressed.js" minified "js/log4javascript.js"

  ))
}
