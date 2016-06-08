package com.kabasoft.iws.gui

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

object IWSPanel {

  case class Props(name:String, body:Seq[ReactElement], header:Seq[ReactElement] = Seq.empty[ReactElement])
  class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props) =
     //Panel(Panel.Props("Customer"), <.div(^.className := "panel-heading",^.padding :=0, ^.width:=950),
    //   <.div(^.padding :=0, ^.width:=900, ^.padding :=5
 
        <.div(^.cls := "container",
          <.h2("Panels with Contextual Classes"),
           
        <.div(^.cls := "panel-group",
          <.div(^.cls := "panel panel-default",
              <.div(^.cls := "panel-heading", "Panel with panel-default class"),
              <.div(^.cls :="panel-body", "Panel Content")
            )

          /*,

            <.div(^.cls :="panel panel-primary",
              <.div(^.cls :="panel-heading", "Panel with panel-primary class"),
              <.div(^.cls :="panel-body", "Panel Content")
            ),
            <.div(^.cls :="panel panel-success",
              <.div(^.cls :="panel-heading", "Panel with panel-success class"),
              <.div(^.cls :="panel-body", "Panel Content")
            ),

            <.div(^.cls :="panel panel-info",
              <.div(^.cls :="panel-heading", "Panel with panel-info class"),
              <.div(^.cls :="panel-body", "Panel Content")
            ),
            <.div(^.cls :="panel panel-warning",
              <.div(^.cls :="panel-heading", "Panel with panel-warning class"),
              <.div(^.cls :="panel-body", "Panel Content")
            ),
            <.div(^.cls :="panel panel-danger",
              <.div(^.cls :="panel-heading", "Panel with panel-danger class"),
              <.div(^.cls :="panel-body", "Panel Content")
            )
           */
          )
        )

  }


  val component = ReactComponentB[Props]("IWSPanel")
    .renderBackend[Backend]
    .build
  def apply(name:String,body:Seq[ReactElement]) = component(Props(name,body))
  def apply(name:String,header:Seq[ReactElement],body:Seq[ReactElement]) = component(Props(name,header,body))
}
