package com.kabasoft.iws.gui

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.language.implicitConversions


object Tab {

  case class TabItem(id: String, name: String, route:String, active:Boolean,content: ReactNode, tabItems:TabItem*)
  //case class TabItem(id: String, name: String, route:String, active:Boolean,content: ReactNode, tabItems:Seq[TabItem]=List.empty[TabItem])
  case class Props(tabs:TabItem)

  val tabItemTitleTag = List(^.cls := "tab-pane fade", ^.role := "tabpanel")
  val tabTitleTag = List(^.cls:="nav nav-tabs nav-pills nav-stacked well",^.id:="nav-tabs-wrapper")


      def buildTabItemRef(item:TabItem) =
         if(item.active)
           <.li( ^.cls:="active", <.a(^.href:=item.route, "data-toggle".reactAttr:="tab", item.name))
         else
           <.li(<.a(^.href:=item.route,"data-toggle".reactAttr:="tab", item.name))

      def buildTabItem(item:TabItem) =
        <.div(tabItemTitleTag, ^.id := item.id,item.content.render)


      def buildX(tab:TabItem)=
      <.div(^.cls :="container",
        <.div(^.cls :="row",
          <.div(^.cls :="col-sm-3",
              <.ul(tabTitleTag,
                tab.tabItems map buildTabItemRef
             )
           ),
            <.div(^.cls:="col-sm-9",
              <.div(^.cls:="tab-content",
                tab.tabItems map buildTabItem
                //buildTab,

              )
            )
        )
      )

  val component = ReactComponentB[Props]("Tab1")
    .stateless
    .render_P(p => {
      buildX(p.tabs)
    }).build



  def apply( props:TabItem) = component(Props(props))


}
