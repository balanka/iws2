package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.macros.Bootstrap.CommonStyle
import com.kabasoft.iws.gui.macros.GlobalStyles
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._

object StockList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(items: Seq[Stock])

  private val storeList = ReactComponentB[Props]("StockList")
    .render_P(p => {
      val style = bss.listGroup
      def renderItem(item: Stock) = {
         <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
          <.span(item.id),
          <.span(item.itemId ,^.paddingLeft:=10),
          <.span("%06.2f".format(item.quantity.toDouble),^.paddingLeft:=10.px),
          <.span("%06.2f".format(item.minStock.toDouble),^.paddingLeft:=10.px)
          )
      }
      <.ul(style.listGroup)(p.items.sortBy(_.id) map renderItem)
    })
    .build

  def apply(items: Seq[Stock]) = storeList(Props(items))
}
