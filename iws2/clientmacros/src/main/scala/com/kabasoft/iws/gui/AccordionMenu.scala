package com.kabasoft.iws.gui

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.EmptyTag
import japgolly.scalajs.react.vdom.prefix_<^._
import scala.language.implicitConversions


object AccordionMenu {

  case class Props(menuItem:MenuItem)
  case class MenuItem(id:String, title:String, route:String, menuItems:Seq[MenuItem]=List.empty[MenuItem])
  val accordionTitleTag = List("data-toggle".reactAttr := "collapse","data-parent".reactAttr:="accordion")
  val menuTitleTag = List(^.cls := "panel-group", ^.id:="accordion", ^.fontWeight:=10,^.fontSize:=12)
  val panelTitleTag = List(^.cls := "panel-title",^.fontWeight:=10,^.fontSize:=12)
  val panelTitle:TagMod = (^.cls := "panel-title").compose( ^.fontWeight:=10).compose(^.fontSize:=12)
  val tabTag:TagMod = List(^.cls :="nav nav-tabs nav-pills nav-stacked well", ^.id:="nav-tabs-wrapper")
  def foldLeft( lx:Seq[TagMod]):TagMod = {
     lx.fold(EmptyTag) { (z, i) =>
       z + i
     }
   }
  def buildPanelTitle(tag:Seq[TagMod])=foldLeft(tag)


  def buildMenuItem(item:MenuItem) =
    <.li(^.className :="list-group-item list-group-item-info",
      <.div(<.a(^.href:= item.route, item.title))
    )

  def  buildSubMenu(v1:MenuItem) =
    <.div(^.className := "panel panel-warning",
      <.div(^.className := "panel-heading",
        <.h4(panelTitle,
          <.a(accordionTitleTag,
            ^.href:= v1.route,  v1.title)
        )
      ),
      //<.div(^.className := "panel-collapse collapse in",^.id:=v1.title,
      <.div(^.className := "panel-collapse collapse",^.id:=v1.title,
        <.div(^.className :="panel-body",
         // <.ul(^.className :="list-group",
             v1.menuItems map buildMenuItem
           )
         // )
        )
    )

  def buildMenu(menu:MenuItem) =
    <.div(^.className := "panel-collapse collapse in",^.id:="accordion1",
    //<.div(^.className := "panel-collapse collapse",^.id:="accordion1",
    <.div(^.className :="panel-body",
      menu.menuItems map buildSubMenu
    )
  )

  def buildMainAccordionMenu(menu:MenuItem)=
    <.div(^.className := "panel panel-primary",
      <.div(^.className := "panel-heading",
       // <.h4(foldLeft(panelTitleTag),
         <.h4(panelTitle,
           <.a( accordionTitleTag,
              ^.href:= "#accordion1",  menu.title)
        )
      ),
      buildMenu(menu)
    )

  val component = ReactComponentB[Props]("Menu")
    .stateless
    .render_P(p => {
      <.div(menuTitleTag, buildMainAccordionMenu(p.menuItem))
    }).build

  def apply(menuItem:MenuItem) = component(Props(menuItem))

}
