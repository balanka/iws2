package com.kabasoft.iws.gui

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.raw.File

import views.html.helper.input
/**
 * Created by weiyin on 16/03/15.
 */
object Test {

/*
  val SelectFile =
    ReactComponentB[File => Callback]("No args")
      .render_P { props =>
        <.div("Hello!",
          "Select input file:",
          <.input(^.`type` := "file", ^.onChange ==> { e: ReactEventI =>
            Callback.alert("File: " + e.target.value + ", files: " + e.target.files.length + ", first: " + e.target.files(0).name ) >>
              Callback.alert("Nr 2") >>
              Callback(
                props(e.target.files(0))
              )
          }
          )
        )
      }.build

  val Results = ReactComponentB[Option[File]]("Results")
    .render_P { props =>
      <.div("Selected file: " + props.map(_.name).getOrElse("N/A"))
    }.build

  val MainView = ReactComponentB[Unit]("Main view")
    .initialState[Option[File]](None)
    .render { $ =>
      <.div(
        SelectFile(
          {
            file => $.modState(_ => Some(file))
          }
        ),
        "Selected filename: " + $.state.map(_.name)
        ,
        Results($.state)
      )
    }.build
    */

  //import scalatags.JsDom.all._
  import org.scalajs.dom
  //import fiddle.Fiddle, Fiddle.println
  import scalajs.js

  object ScalaFiddle { //extends js.JSApp {
    def main() = {
      // $FiddleStart
      val countries = Seq(
        "Macau",
        "Macedonia",
        "Madagascar",
        "Malawi",
        "Malaysia",
        "Maldives",
        "Mali",
        "Malta",
        "Marshall Islands",
        "Mauritania",
        "Mauritius",
        "Mexico",
        "Micronesia",
        "Moldova",
        "Monaco",
        "Mongolia",
        "Montenegro",
        "Morocco",
        "Mozambique"
      )
    def onKeyup1 (e: ReactEventI) = {
      /*result.innerHTML = ""
      result.appendChild(
        ul(
          countries
            .filter(c => c.toLowerCase.startsWith(textbox.value.toLowerCase))
            .map(t => li(t))
        ).render
      )
      */
      Callback.log(s"Item quantity is ${e}")
    }
      val textbox = <.input.text(^.placeholder := "Country", ^.onKeyUp ==> onKeyup1)
      val result = <.div().render



      val content = <.div(
        <.h2("Hello Scala.js"),
        textbox,
        result
      )

      println(content)
      // $FiddleEnd
    }
  }

}
