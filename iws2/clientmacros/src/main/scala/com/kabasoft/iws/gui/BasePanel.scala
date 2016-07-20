package com.kabasoft.iws.gui

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

object BasePanel {

  case class Props(name:String, body:Seq[ReactElement], header:Seq[ReactElement] = Seq.empty[ReactElement])


  class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props) =
     // <.div(^.className := "panel-group",^.id:="accordion", ^.fontWeight:=10,^.fontSize:=12,
      <.div(^.className := "panel panel-primary",
        <.div(^.className := "panel-heading",
          <.h4(^.className := "panel-title", ^.fontWeight:=10,^.fontSize:=12, ^.maxHeight:=10,
            <.a("data-toggle".reactAttr := "collapse","data-parent".reactAttr :="accordion", ^.href:= "#accordion"+p.name,  p.name),
               p.header)),
        buildAccordionPanelBody(p)
      )
   // )

    def buildAccordionPanelBody ( p:Props) =
      //<.div(^.className := "panel-collapse collapse",^.id:="accordion"+p.name,
        <.div(^.className :="panel-body", p.body)
      //)
  }


  val component = ReactComponentB[Props]("AccordionPanel")
    .renderBackend[Backend]
    .build
 def apply(name:String, body:Seq[ReactElement]) = component(Props(name,body))
  def apply(name:String, header:Seq[ReactElement], body:Seq[ReactElement]) = component(Props(name,header,body))
}
