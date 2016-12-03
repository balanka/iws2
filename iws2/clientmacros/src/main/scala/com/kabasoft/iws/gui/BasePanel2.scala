package com.kabasoft.iws.gui

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

object BasePanel2 {

  case class Props(title:Seq[ReactElement], body:Seq[ReactElement])
  class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props) =
      <.div(^.className := " panel with-nav-tabs  panel-default",
         <.div(^.className := "panel-heading clearfix",
                <.h4(^.className := "panel-title", ^.fontWeight:=10,^.fontSize:=12, ^.maxHeight:=10,  p.title)
             ),
         <.div(^.className :="panel-body", p.body)
      )
  }

  val component = ReactComponentB[Props]("AccordionPanel")
    .renderBackend[Backend]
    .build
  def apply(title:Seq[ReactElement], body:Seq[ReactElement]) = component(Props(title,body))
}
