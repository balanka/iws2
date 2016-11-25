package com.kabasoft.iws.gui


import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import scala.language.implicitConversions

case class MenuItem(id:String, title:String, route:String, menuItems:Seq[MenuItem]=List.empty[MenuItem])
object TabAccordionMenu {

  case class Props(menuItem:MenuItem)
  val accordionTitleTag = List("data-toggle".reactAttr := "collapse","data-parent".reactAttr :="accordion")
  val menuTitleTag = List(^.cls := "panel-group", ^.id:="accordion", ^.fontWeight:=10,^.fontSize:=12)
  val panelTitle:TagMod = (^.cls := "panel-title").compose( ^.fontWeight:=10).compose(^.fontSize:=12)

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
          <.div(^.className := "panel-collapse collapse", ^.id := v1.title,
               <.div(^.className :="panel-body", v1.menuItems map buildMenuItem)
              )
          )

  def buildMenu(menu:MenuItem) =
    <.div(^.className := "panel-collapse collapse in",^.id:="accordion1",
      <.div(^.className :="panel-body", menu.menuItems map buildSubMenu)
  )

  val component = ReactComponentB[Props]("Menu")
    .stateless
    .render_P(p => {
      <.div(menuTitleTag, buildMenu(p.menuItem))
    }).build

  def apply(menuItem:MenuItem) = component(Props(menuItem))

}


object TabAcordionMenu2 {

  case class Props(menuItem:MenuItem)
  val accordionTitleTag = List("data-toggle".reactAttr := "collapse","data-parent".reactAttr :="accordion")
  val menuTitleTag = List(^.className := "panel-group fa fa-sitemap", ^.id:="accordion", ^.fontWeight:=10,^.fontSize:=12)
  //val panelTitleTag = List(^.className := "panel-title",^.fontWeight:=10,^.fontSize:=12)
  val panelTitle:TagMod = (^.className := "panel-title").compose( ^.fontWeight:=10).compose(^.fontSize:=12)
  //val tabTag:TagMod = List(^.className :="nav nav-tabs nav-pills nav-stacked well", ^.id:="nav-tabs-wrapper")
  //val tabItemTitleTag = List(^.cls := "tab-pane fade", ^.role:= "tabpanel")

  def buildMenuItem(item:MenuItem) =
    <.li(^.className :="list-group-item list-group-item-info",
      <.div(<.a(^.href:= item.route, item.title))
    )
  def  buildSubMenu(v1:MenuItem) =
    <.div(^.className := "panel panel-warning",
      <.div(^.className := "panel-heading",
        // <.span(^.className :="glyphicon glyphicon-th",
        <.h4(panelTitle,
          <.a(accordionTitleTag,
            ^.href:= v1.route,  v1.title)
        )
      ),
      //<.div(^.className := "panel-collapse collapse in",^.id:=v1.title,
      <.div(^.className := "panel-collapse collapse",^.id:=v1.title,
        <.div(^.className :="panel-body", v1.menuItems map buildMenuItem)
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
