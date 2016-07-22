package com.kabasoft.iws.gui.macros

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

case class TabItem (id:String, title:String, route:String , active:Boolean,content:ReactElement )
object TabComponent {

  case class Props(items: Seq[TabItem])

  def buildTab (items:Seq[TabItem] ):ReactElement  =
    <.div(^.cls:="container-fluid",
      <.div(^.cls:="row", ^.padding:=2.px,
        <.div(^.cls:="col-sm-2",  ^.padding:=1.px,
          buildTabHeader(items)),
        buildTabContent(items))
    )

  def buildIt(item:TabItem) ={
    var titleTag = "tab-pane fade"
    if (item.active) titleTag = "tab-pane fade in active"
    <.div(^.role := "tabpanel", ^.cls := titleTag,  ^.id := item.id, item.content)
  }
  def buildTabContent( tabItems: Seq[TabItem])=
    <.div(^.cls := "col-sm-10", ^.padding:=1.px,
      <.div(^.cls := "tab-content",  tabItems map (item => buildIt(item))
      )
    )

  def buildTabHeader( tabItems: Seq[TabItem])=
    <.ul(^.id:="nav-tabs-wrapper", ^.cls:="nav nav-tabs nav-pills nav-stacked well",
      tabItems map (item =>
        if(item.active)
          <.li(^.cls := "active", <.a(^.href :=item.route, "data-toggle".reactAttr := "tab", item.title))
        else
          <.li(<.a(^.href:=item.route, "data-toggle".reactAttr:="tab", item.title)))
    )

  val tab = ReactComponentB[Props]("Tab")
    .render_P (p => buildTab(p.items))
    .build

  def apply(items: Seq[TabItem]) = tab(Props(items))

}
object TabComponent2 {

  case class Props(title:String, items: Seq[TabItem],  header:Seq[ReactElement] = Seq.empty[ReactElement])

  def buildTab (p:Props ):ReactElement  =
    <.div(^.cls:="container-fluid",
      <.div(^.cls:="row", ^.padding:=2.px,
        <.div(^.cls:= "col-md-12",
           <.div(^.cls:= "panel with-nav-tabs panel-default", ^.padding:=2.px,
          //<.div(^.cls:= "panel with-nav-tabs panel-warning", ^.padding:=2.px,
            buildTabHeader(p),
            buildTabContent(p.items)
          )
      )
    )
  )


  def buildTabContent( tabItems: Seq[TabItem]) = {
    var titleTag = "tab-pane fade"
    <.div(^.cls := "panel-body",
      <.div(^.cls := "tab-content", tabItems map (item => {
        if (item.active) titleTag = "tab-pane fade in active"
              <.div(^.role := "tabpanel", ^.cls := titleTag, ^.id := item.id, item.content)
           }
          )
        )
    )
  }
  def buildTabHeader( p:Props) =
    <.div(^.cls := "panel-heading ",
      <.h2(^.cls := "pull-left panel-title",  p.title,  ^.fontWeight:=10,^.fontSize:=12, ^.maxHeight:=10,^.paddingRight:=20.px),
      <.span(^.cls := "btn pull-right", p.header),
      <.ul(^.cls:= "nav nav-tabs", p.items  map (item =>
        if(item.active)
          <.li(^.cls := "active", <.a(^.href :=item.route, "data-toggle".reactAttr := "tab", item.title))
        else
          <.li(<.a(^.href:=item.route, "data-toggle".reactAttr:="tab", item.title)))
     )
    )

  val tab = ReactComponentB[Props]("Tab2")
    .render_P (p => buildTab(p))
    .build

  def apply(title:String, items: Seq[TabItem],header: Seq[ReactElement]) = tab(Props(title, items,header))

}
