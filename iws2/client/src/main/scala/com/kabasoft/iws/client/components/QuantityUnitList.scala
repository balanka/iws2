package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.macros.GlobalStyles
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import scalacss.ScalaCssReact._

object QuantityUnitList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class QuantityUnitListProps(
    items: Seq[IWS],
    stateChange: IWS => Callback,
    editItem: IWS => Callback,
    deleteItem: IWS => Callback
  )

  private val QuantityUnitList = ReactComponentB[QuantityUnitListProps]("QuantityUnitList")
    .render_P(p => {
      val style = bss.listGroup
      def renderItem(item: QuantityUnit) = {
        <.li(style.itemOpt(CommonStyle.info),
          <.span(" "),
          <.s(item.id),
          <.span(" "),
          <.span(item.name),
          <.span(" "),
          <.s(item.description),
          Button(Button.Props(p.editItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS)), "Edit"),
          Button(Button.Props(p.deleteItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS)), "Delete")
        )
      }
      <.ul(style.listGroup)(p.items.asInstanceOf[Seq[QuantityUnit]].sortBy(_.id) map renderItem)
    })
    .build

  def apply(items: Seq[IWS], stateChange: IWS => Callback, editItem: IWS => Callback, deleteItem: IWS => Callback) =
    QuantityUnitList(QuantityUnitListProps(items, stateChange, editItem, deleteItem))
}
