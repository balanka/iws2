package com.kabasoft.iws.gui.macros

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

case class TabItem (id:String, title:String, route:String , active:Boolean,content:ReactElement )
object TabComponent {

  case class Props(items: Seq[TabItem])

  def buildTab (items:Seq[TabItem] ):ReactElement  =
    <.div(^.cls:="container",
      <.div(^.cls:="row",
        <.div(^.cls:="col-sm-2",
            buildTabHeader(items)),
            buildTabContent(items))
    )

  def buildIt(item:TabItem) ={
    var titleTag = "tab-pane fade"
    if (item.active) titleTag = "tab-pane fade in active"
    <.div(^.role := "tabpanel", ^.cls := titleTag, ^.id := item.id, item.content)
  }
  def buildTabContent( tabItems: Seq[TabItem])=
    <.div(^.cls := "col-sm-6",
      <.div(^.cls := "tab-content", tabItems map (item => buildIt(item))
    )
   )

  def buildTabHeader( tabItems: Seq[TabItem])=
    <.ul(^.id:="nav-tabs-wrapper", ^.cls:="nav nav-tabs nav-pills nav-stacked well",
    //<.ul(^.id:="nav-tabs-wrapper", ^.cls:="nav nav-tabs nav-pills nav-stacked col-sm-3 col-sm-push-9",
      tabItems map (item =>
        if(item.active)
          <.li(^.cls := "active", <.a(^.href :=item.route, "data-toggle".reactAttr := "tab", item.title))
        else
          <.li(<.a(^.href:=item.route, "data-toggle".reactAttr:="tab", item.title)))
    )


  val tab = ReactComponentB[Props]("Tab")
    .render_P { p =>
      buildTab(p.items)

    }
    .build

  def apply(items: Seq[TabItem]) = tab(Props(items))
  //def apply2(items2: Seq[TabItem2]) = Tab2(Props2(items2))
}
