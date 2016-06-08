package com.kabasoft.iws.gui


import com.kabasoft.iws.gui.macros.GlobalStyles
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap.{Panel, CommonStyle}
import scalacss.ScalaCssReact._


object Navigator {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class NavItem(name: String, route: String, menuItem:Seq[NavItem]=List.empty[NavItem])
  case class Props(menuItem:Seq[NavItem])
  val component = ReactComponentB[Props]("Navigator")
    .render_P(p => {
      val style = bss.listGroup
      def renderItem(item: NavItem) = {
       // Panel(Panel.Props("IWS"),
          //<.a(Attr("data-toggle") := "collapse", ^.href:= "#Panel1",  "Collapsible List"),
         // ,<.div(^.id:="Panel1", ^.className :="panel-collapse collapse",
       // <.tr(style.itemOpt(Co mmonStyle.success),^.backgroundColor:="#cce6ff",^.fontSize:=12,^.fontStyle:="strong",^.fontWeight:=10,^.maxHeight:=30,
        <.tr(^.fontWeight:=10,^.fontStyle:="strong",
          //<.td(<.a(Attr("data-toggle") := "collapse", ^.href:=item.name,item.route))
          <.td(<.div(^.id:=item.name, ^.className :="panel-collapse collapse",
          <.td( <.a(^.href :=item.name, item.route))
          )
          )
        )
        //  )
       // )
      }

      //<.table(style.listGroup, ^.`class`:="table-striped table-hover", ^.paddingTop:=50)(p.menuItem map renderItem)
     // Panel(Panel.Props("IWS"),(p.menuItem map renderItem))
      Panel(Panel.Props("IWS"),<.table(style.listGroup, ^.`class`:="table-striped table-hover table-inverse",^.backgroundColor:="#cce6ff")(p.menuItem map renderItem))
    })
    .build

  def apply(menuItem:Seq[NavItem]) = component(Props(menuItem))

}
